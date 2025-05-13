package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.example.travelapp.Domain.ItemDomain;
import com.example.travelapp.databinding.ActivityDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private ItemDomain object;
    private DatabaseReference dbRef;
    private FirebaseAuth mAuth;
    private WishlistManager wishlistManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        wishlistManager = new WishlistManager(this, binding.imageView8);

        getIntentExtra();
        setVariable();
        wishlistManager.checkIfInWishlist(String.valueOf(object.getId()));
    }

    private void setVariable() {
        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText("$" + object.getPrice());
        binding.backBtn.setOnClickListener(v -> finish());
        binding.bedTxt.setText("" + object.getBed());
        binding.durationTxt.setText(object.getDuration());
        binding.distanceTxt.setText(object.getDistance());
        binding.descriptionTxt.setText(object.getDescription());
        binding.addressTxt.setText(object.getAddress());
        binding.ratingTxt.setText(object.getScore() + " Rating");
        binding.ratingBar.setRating((float) object.getScore());

        Glide.with(DetailActivity.this)
                .load(object.getPic())
                .into(binding.pic);

        binding.addToCartBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, DatePickerActivity.class);
            intent.putExtra("itemId", String.valueOf(object.getId()));
            startActivity(intent);
        });

        binding.imageView8.setOnClickListener(v -> wishlistManager.toggleWishlist(String.valueOf(object.getId())));
    }

    private void getIntentExtra() {
        object = (ItemDomain) getIntent().getSerializableExtra("object");
    }
}