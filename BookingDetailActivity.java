package com.example.travelapp.Activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.travelapp.Domain.Booking;
import com.example.travelapp.R;

public class BookingDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        // Ánh xạ các view từ layout
        ImageView backBtn = findViewById(R.id.backBtn);
        TextView detailTourName = findViewById(R.id.detail_tour_name);
        TextView detailAddress = findViewById(R.id.detail_address);
        TextView detailDate = findViewById(R.id.detail_date);
        TextView detailGuestName = findViewById(R.id.detail_guest_name);
        TextView detailGuestNumber = findViewById(R.id.detail_guest_number);
        TextView detailPhone = findViewById(R.id.detail_phone);
        TextView detailEmail = findViewById(R.id.detail_email);
        TextView detailTotal = findViewById(R.id.detail_total);
        TextView detailPrice = findViewById(R.id.detail_price);
        TextView detailBed = findViewById(R.id.detail_bed);
        TextView detailDistance = findViewById(R.id.detail_distance);
        TextView detailScore = findViewById(R.id.detail_score);
        TextView detailTimeTour = findViewById(R.id.detail_time_tour);
        TextView detailTourGuideName = findViewById(R.id.detail_tour_guide_name);
        TextView detailTourGuidePhone = findViewById(R.id.detail_tour_guide_phone);
        ImageView detailImage = findViewById(R.id.detail_image);
        ImageView detailTourGuideImage = findViewById(R.id.detail_tour_guide_image);

        // Xử lý sự kiện nút back
        backBtn.setOnClickListener(v -> finish());

        // Lấy dữ liệu từ Intent
        Booking booking = (Booking) getIntent().getSerializableExtra("booking");

        // Hiển thị dữ liệu
        if (booking != null) {
            detailTourName.setText(booking.getTourName());
            detailAddress.setText(booking.getAddress());
            detailDate.setText(booking.getDate());
            detailGuestName.setText(booking.getGuestName());
            detailGuestNumber.setText(String.valueOf(booking.getGuestNumber()));
            detailPhone.setText(booking.getPhone());
            detailEmail.setText(booking.getEmail());
            detailTotal.setText("$" + booking.getTotal());
            detailPrice.setText("$" + booking.getPrice());
            detailBed.setText(String.valueOf(booking.getBed()));
            detailDistance.setText(booking.getDistance());
            detailScore.setText(String.valueOf(booking.getScore()));
            detailTimeTour.setText(booking.getTimeTour());
            detailTourGuideName.setText(booking.getTourGuideName());
            detailTourGuidePhone.setText(booking.getTourGuidePhone());

            // Tải ảnh tour bằng Glide
            if (booking.getPic() != null) {
                Glide.with(this)
                        .load(booking.getPic())
                        .into(detailImage);
            }

            // Tải ảnh tour guide bằng Glide
            if (booking.getTourGuidePic() != null) {
                Glide.with(this)
                        .load(booking.getTourGuidePic())
                        .into(detailTourGuideImage);
            }
        }
    }
}