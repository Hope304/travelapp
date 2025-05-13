package com.example.travelapp.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.travelapp.Activity.MainActivity;
import com.example.travelapp.R;
import com.example.travelapp.SharedPrefsHelper;
import com.example.travelapp.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private String userId;
    private ChipNavigationBar bottomNav;
    private SharedPrefsHelper prefsHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("users");
        prefsHelper = new SharedPrefsHelper(requireContext());

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        } else {
            Log.e("ProfileFragment", "User not logged in");
            prefsHelper.clearLogin();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new LoginFragment())
                    .commit();
            return view;
        }

        bottomNav = getActivity().findViewById(R.id.bottom_nav);
        if (bottomNav == null) {
            Log.e("ProfileFragment", "ChipNavigationBar not found in MainActivity");
        }

        loadUserProfile();

        if (binding.bookingItem != null) {
            binding.bookingItem.setOnClickListener(v -> navigateToBooking());
        } else {
            Log.e("ProfileFragment", "bookingItem is null");
        }
        if (binding.wishlistItem != null) {
            binding.wishlistItem.setOnClickListener(v -> navigateToWishlist());
        } else {
            Log.e("ProfileFragment", "wishlistItem is null");
        }
        if (binding.editProfileItem != null) {
            binding.editProfileItem.setOnClickListener(v -> navigateToEditProfile());
        } else {
            Log.e("ProfileFragment", "editProfileItem is null");
        }
        if (binding.logoutItem != null) {
            binding.logoutItem.setOnClickListener(v -> logout());
        } else {
            Log.e("ProfileFragment", "logoutItem is null");
        }

        return view;
    }

    private void loadUserProfile() {
        if (userId == null) return;

        dbRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String avatar = snapshot.child("avatar").getValue(String.class);

                    binding.userName.setText(name != null ? name : "User");
                    binding.userLocation.setText(email != null ? email : "No email");
                    if (avatar != null && !avatar.isEmpty()) {
                        Picasso.get().load(avatar).into(binding.profileImage);
                    }
                } else {
                    Log.e("ProfileFragment", "No user data found");
                    binding.userName.setText("Not Logged In");
                    binding.userLocation.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ProfileFragment", "Error loading user data: " + error.getMessage());
            }
        });
    }

    private void navigateToBooking() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new BookingFragment())
                .addToBackStack(null)
                .commit();
        if (bottomNav != null) {
            bottomNav.setItemSelected(R.id.booking, true);
        }
    }

    private void navigateToWishlist() {
        ((MainActivity) getActivity()).loadFragment(new WishlistFragment());
        if (bottomNav != null) {
            bottomNav.setItemSelected(R.id.bookmark, true);
        }
    }

    private void navigateToEditProfile() {
        EditProfileFragment editProfileFragment = new EditProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        editProfileFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, editProfileFragment)
                .addToBackStack(null)
                .commit();
    }

    private void logout() {
        mAuth.signOut();
        prefsHelper.clearLogin();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (binding != null) {
            binding.getRoot().requestFocus();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}