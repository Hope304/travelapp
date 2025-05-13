package com.example.travelapp.Activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.travelapp.Fragment.BookingFragment;
import com.example.travelapp.Fragment.WishlistFragment;
import com.example.travelapp.Fragment.HomeFragment;
import com.example.travelapp.Fragment.LoginFragment;
import com.example.travelapp.Fragment.ProfileFragment;
import com.example.travelapp.R;
import com.example.travelapp.SharedPrefsHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends BaseActivity {
    private SharedPrefsHelper prefsHelper;
    private FirebaseAuth mAuth;
    private ChipNavigationBar bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefsHelper = new SharedPrefsHelper(this);
        mAuth = FirebaseAuth.getInstance();
        bottomNav = findViewById(R.id.bottom_nav);

        // Kiểm tra và đồng bộ trạng thái đăng nhập
        syncLoginState();

        updateNavigationMenu();
        bottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                if (id == R.id.homebtn) {
                    loadFragment(new HomeFragment());
                } else if (id == R.id.booking) {
                    loadFragment(new BookingFragment());
                } else if (id == R.id.bookmark) {
                    loadFragment(new WishlistFragment());
                } else if (id == R.id.profile) {
                    loadFragment(new ProfileFragment());
                } else if (id == R.id.login) {
                    loadFragment(new LoginFragment());
                }
            }
        });

        if (savedInstanceState == null) {
            bottomNav.setItemSelected(R.id.homebtn, true);
            loadFragment(new HomeFragment());
        }
    }

    private void syncLoginState() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        boolean isLoggedInInPrefs = prefsHelper.isLoggedIn();
        if (!isLoggedInInPrefs && currentUser != null) {
            // Trường hợp hiếm: SharedPrefs chưa cập nhật nhưng Firebase có user
            prefsHelper.setLoggedIn(true);
        } else if (isLoggedInInPrefs && currentUser == null) {
            // Trường hợp hiện tại: SharedPrefs ghi nhận đăng nhập nhưng Firebase không có user
            prefsHelper.clearLogin();
        }
    }

    private void updateNavigationMenu() {
        if (prefsHelper.isLoggedIn()) {
            bottomNav.setMenuResource(R.menu.menu_bottom); // Menu với Profile
        } else {
            bottomNav.setMenuResource(R.menu.menu_bottom_logged_out); // Menu với Login
        }
    }

    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        syncLoginState(); // Đồng bộ trạng thái khi quay lại activity
        updateNavigationMenu();
    }
}