package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelapp.SharedPrefsHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends BaseActivity {
    private FirebaseAuth mAuth;
    private SharedPrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        prefsHelper = new SharedPrefsHelper(this);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intent;
        if (currentUser != null && prefsHelper.isLoggedIn()) {
            // Nếu đã đăng nhập, chuyển thẳng đến MainActivity
            intent = new Intent(this, MainActivity.class);
        } else {
            // Nếu chưa đăng nhập, chuyển đến IntroActivity
            intent = new Intent(this, IntroActivity.class);
        }

        startActivity(intent);
        finish();
    }
}