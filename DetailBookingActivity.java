package com.example.travelapp.Activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.travelapp.Domain.Booking;
import com.example.travelapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DetailBookingActivity extends BaseActivity {

    private TextView confirmTitle, confirmAddress, confirmDate, confirmPrice, confirmTotal,
            confirmBed, confirmDescription, confirmDistance, confirmDuration,
            confirmTimeTour, confirmTourGuideName, confirmTourGuidePhone;
    private ImageView confirmPic, confirmTourGuidePic;
    private EditText guestName, guestNumber, guestPhone, guestEmail;
    private Button cancelBtn, confirmBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private DatabaseReference itemRef;
    private String userId;
    private String itemId;
    private String selectedDate;
    private int pricePerPerson;
    // Biến để lưu thông tin của Item
    private String title, address, description, distance, duration, timeTour, tourGuideName, tourGuidePhone, pic, tourGuidePic;
    private int bed, id;
    private double score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_booking);

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("bookings");
        itemRef = FirebaseDatabase.getInstance().getReference("Item");

        // Kiểm tra trạng thái đăng nhập
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please log in to continue", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        userId = currentUser.getUid();

        // Lấy dữ liệu từ Intent
        selectedDate = getIntent().getStringExtra("selectedDate");
        itemId = getIntent().getStringExtra("itemId");

        // Kiểm tra itemId
        if (itemId == null) {
            Toast.makeText(this, "Item ID is null", Toast.LENGTH_SHORT).show();
            Log.e("DetailBooking", "Item ID is null");
            finish();
            return;
        }
        Log.d("DetailBooking", "Item ID: " + itemId);

        // Khởi tạo các view
        confirmTitle = findViewById(R.id.confirm_title);
        confirmAddress = findViewById(R.id.confirm_address);
        confirmDate = findViewById(R.id.confirm_date);
        confirmPrice = findViewById(R.id.confirm_price);
        confirmTotal = findViewById(R.id.confirm_total);
        confirmBed = findViewById(R.id.confirm_bed);
        confirmDescription = findViewById(R.id.confirm_description);
        confirmDistance = findViewById(R.id.confirm_distance);
        confirmDuration = findViewById(R.id.confirm_duration);
        confirmTimeTour = findViewById(R.id.confirm_time_tour);
        confirmTourGuideName = findViewById(R.id.confirm_tour_guide_name);
        confirmTourGuidePhone = findViewById(R.id.confirm_tour_guide_phone);
        confirmPic = findViewById(R.id.confirm_pic);
        confirmTourGuidePic = findViewById(R.id.confirm_tour_guide_pic);
        guestName = findViewById(R.id.guest_name);
        guestNumber = findViewById(R.id.guest_number);
        guestPhone = findViewById(R.id.guest_phone);
        guestEmail = findViewById(R.id.guest_email);
        cancelBtn = findViewById(R.id.cancel_btn);
        confirmBtn = findViewById(R.id.confirm_btn);

        // Áp dụng background thủ công cho confirmBtn
        try {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setColor(0xFF2196F3); // Màu xanh
            drawable.setCornerRadius(8f);
            confirmBtn.setBackground(drawable);
            Log.d("DetailBooking", "Applied manual background to confirmBtn");
        } catch (Exception e) {
            Log.e("DetailBooking", "Error applying background: " + e.getMessage());
        }

        // Lấy dữ liệu từ Firebase
        itemRef.child(itemId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    title = snapshot.child("title").getValue(String.class);
                    address = snapshot.child("address").getValue(String.class);
                    pricePerPerson = snapshot.child("price").getValue(Integer.class) != null ? snapshot.child("price").getValue(Integer.class) : 0;
                    bed = snapshot.child("bed").getValue(Integer.class) != null ? snapshot.child("bed").getValue(Integer.class) : 0;
                    description = snapshot.child("description").getValue(String.class);
                    distance = snapshot.child("distance").getValue(String.class);
                    duration = snapshot.child("duration").getValue(String.class);
                    id = snapshot.child("id").getValue(Integer.class) != null ? snapshot.child("id").getValue(Integer.class) : 0;
                    score = snapshot.child("score").getValue(Double.class) != null ? snapshot.child("score").getValue(Double.class) : 0.0;
                    timeTour = snapshot.child("timeTour").getValue(String.class);
                    tourGuideName = snapshot.child("tourGuideName").getValue(String.class);
                    tourGuidePhone = snapshot.child("tourGuidePhone").getValue(String.class);
                    pic = snapshot.child("pic").getValue(String.class);
                    tourGuidePic = snapshot.child("tourGuidePic").getValue(String.class);

                    // Kiểm tra dữ liệu lấy được
                    Log.d("DetailBooking", "Title: " + title + ", Address: " + address + ", Price: " + pricePerPerson);

                    // Hiển thị thông tin
                    confirmTitle.setText(title != null ? title : "N/A");
                    confirmAddress.setText(address != null ? address : "N/A");
                    confirmDate.setText(selectedDate);
                    confirmPrice.setText("$" + pricePerPerson);
                    confirmBed.setText(bed != 0 ? "Beds: " + bed : "Beds: N/A");
                    confirmDescription.setText(description != null ? description : "Description: N/A");
                    confirmDistance.setText(distance != null ? "Distance: " + distance : "Distance: N/A");
                    confirmDuration.setText(duration != null ? "Duration: " + duration : "Duration: N/A");
                    confirmTimeTour.setText(timeTour != null ? "Time: " + timeTour : "Time: N/A");
                    confirmTourGuideName.setText(tourGuideName != null ? "Tour Guide: " + tourGuideName : "Tour Guide: N/A");
                    confirmTourGuidePhone.setText(tourGuidePhone != null ? "Guide Phone: " + tourGuidePhone : "Guide Phone: N/A");

                    // Tải ảnh bằng Glide
                    if (pic != null) {
                        Glide.with(DetailBookingActivity.this)
                                .load(pic)
                                .into(confirmPic);
                    }
                    if (tourGuidePic != null) {
                        Glide.with(DetailBookingActivity.this)
                                .load(tourGuidePic)
                                .into(confirmTourGuidePic);
                    }

                    // Đặt giá trị mặc định cho Guest Number là 1 và cập nhật Total
                    guestNumber.setText("1");
                    int total = pricePerPerson * 1;
                    confirmTotal.setText("$" + total);
                } else {
                    Toast.makeText(DetailBookingActivity.this, "Item not found in database", Toast.LENGTH_SHORT).show();
                    Log.e("DetailBooking", "Item not found for ID: " + itemId);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailBookingActivity.this, "Failed to load item: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("DetailBooking", "Database error: " + error.getMessage());
                finish();
            }
        });

        // Tính tổng tiền khi nhập Guest Number
        guestNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String numberStr = s.toString().trim();
                try {
                    int number = numberStr.isEmpty() ? 0 : Integer.parseInt(numberStr);
                    int total = number * pricePerPerson;
                    confirmTotal.setText("$" + total);
                } catch (NumberFormatException e) {
                    confirmTotal.setText("$0");
                }
            }
        });

        // Xử lý nút Cancel
        cancelBtn.setOnClickListener(v -> finish());

        // Xử lý nút Confirm
        confirmBtn.setOnClickListener(v -> {
            addToBooking();
            finish();
        });

        // Xử lý chạm ra ngoài để ẩn bàn phím
        findViewById(android.R.id.content).getRootView().setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                hideKeyboard();
                guestName.clearFocus();
                guestNumber.clearFocus();
                guestPhone.clearFocus();
                guestEmail.clearFocus();
            }
            return false;
        });
    }

    private void addToBooking() {
        if (userId == null || selectedDate == null || pricePerPerson == 0) {
            Toast.makeText(this, "Invalid booking data", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo ID duy nhất cho mục đặt chỗ
        String bookingId = dbRef.push().getKey();
        if (bookingId == null) {
            Toast.makeText(this, "Failed to generate booking ID", Toast.LENGTH_SHORT).show();
            return;
        }

        String guestNameText = guestName.getText().toString().trim();
        String guestNumberText = guestNumber.getText().toString().trim();
        String phone = guestPhone.getText().toString().trim();
        String email = guestEmail.getText().toString().trim();
        String totalStr = confirmTotal.getText().toString().replace("$", "");

        // Kiểm tra dữ liệu đầu vào
        if (guestNameText.isEmpty() || guestNumberText.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int guestNumber = Integer.parseInt(guestNumberText);
            int totalPrice = Integer.parseInt(totalStr);

            // Tạo đối tượng Booking với toàn bộ thông tin của Item
            Booking booking = new Booking(
                    title, address, selectedDate, guestNameText, guestNumber, phone, email, totalPrice,
                    pic, pricePerPerson, bed, description, distance, duration, id, score,
                    timeTour, tourGuideName, tourGuidePhone, tourGuidePic
            );

            // Lưu vào Firebase
            dbRef.child(userId).child(bookingId).setValue(booking)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(DetailBookingActivity.this, "Booking confirmed successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(DetailBookingActivity.this, "Failed to confirm booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("DetailBooking", "Booking save failed: " + e.getMessage());
                    });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid guest number or total amount", Toast.LENGTH_SHORT).show();
            Log.e("DetailBooking", "NumberFormatException: " + e.getMessage());
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}