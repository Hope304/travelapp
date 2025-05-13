package com.example.travelapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cloudinary.Cloudinary;
import com.example.travelapp.CloudinaryConfig;
import com.example.travelapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EditProfileFragment extends Fragment {

    private EditText editName, editPhone, editAge;
    private ImageView profileImage;
    private Button selectImageButton, saveButton;
    private ProgressBar progressBar;
    private DatabaseReference dbRef;
    private String userId;
    private Uri imageUri;
    private Cloudinary cloudinary;

    // ActivityResultLauncher để chọn ảnh
    private final ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    imageUri = uri;
                    profileImage.setImageURI(uri);
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        editName = view.findViewById(R.id.edit_name);
        editPhone = view.findViewById(R.id.edit_phone);
        editAge = view.findViewById(R.id.edit_age);
        profileImage = view.findViewById(R.id.profile_image);
        selectImageButton = view.findViewById(R.id.select_image_button);
        saveButton = view.findViewById(R.id.save_button);
        progressBar = view.findViewById(R.id.progress_bar);

        dbRef = FirebaseDatabase.getInstance().getReference("users");
        cloudinary = CloudinaryConfig.getInstance();

        // Lấy userId từ Bundle
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
        }

        if (userId == null) {
            Toast.makeText(getContext(), "Error: User ID not found", Toast.LENGTH_SHORT).show();
            return view;
        }

        loadCurrentData();

        selectImageButton.setOnClickListener(v -> pickImage());
        saveButton.setOnClickListener(v -> saveProfile());

        return view;
    }

    private void loadCurrentData() {
        dbRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    // Handle age as Object to support Integer or Long
                    Object ageObj = snapshot.child("age").getValue();
                    String ageStr = "";
                    if (ageObj instanceof Integer) {
                        ageStr = String.valueOf((Integer) ageObj);
                    } else if (ageObj instanceof Long) {
                        ageStr = String.valueOf((Long) ageObj);
                    } else if (ageObj instanceof String) {
                        ageStr = (String) ageObj;
                    }

                    editName.setText(name != null ? name : "");
                    editPhone.setText(phone != null ? phone : "");
                    editAge.setText(ageStr);
                    String avatar = snapshot.child("avatar").getValue(String.class);
                    if (avatar != null && !avatar.isEmpty()) {
                        Picasso.get().load(avatar).into(profileImage);
                    }
                } else {
                    Log.w("EditProfileFragment", "No user data found for userId: " + userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EditProfileFragment", "Failed to load user data: " + error.getMessage());
                Toast.makeText(getContext(), "Failed to load profile data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pickImage() {
        pickImageLauncher.launch("image/*");
    }

    private void saveProfile() {
        String name = editName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String ageStr = editAge.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || ageStr.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Age must be a number", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("phone", phone);
        updates.put("age", age);

        // Nếu có ảnh mới, tải lên Cloudinary
        if (imageUri != null) {
            new Thread(() -> {
                try {
                    Context context = requireContext();
                    InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
                    if (inputStream != null) {
                        Map uploadParams = new HashMap();
                        uploadParams.put("resource_type", "image");
                        uploadParams.put("secure", true); // Yêu cầu URL https
                        Map uploadResult = cloudinary.uploader().upload(inputStream, uploadParams);
                        String imageUrl = (String) uploadResult.get("url");

                        // Kiểm tra và đảm bảo URL là https
                        if (!imageUrl.startsWith("https://")) {
                            imageUrl = imageUrl.replace("http://", "https://");
                        }

                        updates.put("avatar", imageUrl);

                        // Cập nhật dữ liệu vào Realtime Database
                        requireActivity().runOnUiThread(() -> updateUserData(updates));
                    } else {
                        throw new Exception("Failed to open input stream");
                    }
                } catch (Exception e) {
                    requireActivity().runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }).start();
        } else {
            // Không có ảnh mới, chỉ cập nhật các trường khác
            updateUserData(updates);
        }
    }

    private void updateUserData(Map<String, Object> updates) {
        dbRef.child(userId).updateChildren(updates)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Log.e("EditProfileFragment", "Failed to update profile: " + e.getMessage());
                    Toast.makeText(getContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                });
    }
}