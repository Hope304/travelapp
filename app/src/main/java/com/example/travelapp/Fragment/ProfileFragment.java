package com.example.travelapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.travelapp.Activity.EditProfileActivity;
import com.example.travelapp.R;
import com.example.travelapp.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        loadUserProfile();

        // Set click listeners
        binding.bookingItem.setOnClickListener(v -> navigateToBooking());
        binding.wishlistItem.setOnClickListener(v -> navigateToWishlist());
        binding.editProfileItem.setOnClickListener(v -> navigateToEditProfile());
        binding.changeLanguageItem.setOnClickListener(v -> showChangeLanguageDialog());
        binding.colorModeItem.setOnClickListener(v -> toggleColorMode());
        binding.logoutItem.setOnClickListener(v -> logout());

        return view;
    }

    private void loadUserProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName() != null ? user.getDisplayName() : "User";
            String email = user.getEmail() != null ? user.getEmail() : "No email";
            binding.userName.setText(name);
            binding.userLocation.setText(email); // Có thể thay bằng địa chỉ thực tế nếu có
        } else {
            Log.e("ProfileFragment", "User not logged in");
            binding.userName.setText("Not Logged In");
            binding.userLocation.setText("");
        }
    }

    private void navigateToBooking() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.navigation_booking);
    }

    private void navigateToWishlist() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.navigation_wishlist);
    }

    private void navigateToEditProfile() {
        startActivity(new Intent(getContext(), EditProfileActivity.class));
    }

    private void showChangeLanguageDialog() {
        // TODO: Implement language selection dialog
        Log.d("ProfileFragment", "Change language clicked");
    }

    private void toggleColorMode() {
        // TODO: Implement color mode toggle (e.g., light/dark theme)
        Log.d("ProfileFragment", "Color mode clicked");
    }

    private void logout() {
        mAuth.signOut();
        Navigation.findNavController(binding.getRoot()).navigate(R.id.navigation_login);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}