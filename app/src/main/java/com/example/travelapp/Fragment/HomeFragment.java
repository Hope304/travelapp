package com.example.travelapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.travelapp.Adapter.CategoryAdapter;
import com.example.travelapp.Adapter.PopularAdapter;
import com.example.travelapp.Adapter.RecommendedAdapter;
import com.example.travelapp.Adapter.SliderAdapter;
import com.example.travelapp.Domain.Category;
import com.example.travelapp.Domain.ItemDomain;
import com.example.travelapp.Domain.SliderItems;
import com.example.travelapp.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private FirebaseDatabase database;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo Firebase Database
        database = FirebaseDatabase.getInstance();

        // Gọi các phương thức khởi tạo
//        initLocation();
        initBanner();
        initCategory();
        initRecommended();
        initPopular();
    }

//    private void initLocation() {
//        DatabaseReference myRef = database.getReference("Location");
//        ArrayList<Location> list = new ArrayList<>();
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (!isAdded() || getContext() == null || binding == null) {
//                    return; // Thoát nếu Fragment hoặc binding không còn hợp lệ
//                }
//
//                if (snapshot.exists()) {
//                    for (DataSnapshot issue : snapshot.getChildren()) {
//                        list.add(issue.getValue(Location.class));
//                    }
//                    if (!list.isEmpty()) {
//                        ArrayAdapter<Location> adapter = new ArrayAdapter<>(requireContext(), R.layout.sp_item, list);
//                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        binding.locationSp.setAdapter(adapter);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                if (isAdded() && getContext() != null && binding != null) {
//                    // Xử lý lỗi nếu cần
//                }
//            }
//        });
//    }

    private void initPopular() {
        DatabaseReference myRef = database.getReference("Popular");
        if (binding != null) {
            binding.progressBarPopular.setVisibility(View.VISIBLE);
        }

        ArrayList<ItemDomain> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!isAdded() || getContext() == null || binding == null) {
                    return;
                }

                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(ItemDomain.class));
                    }
                    if (!list.isEmpty()) {
                        binding.recyclerViewPopular.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
                        RecyclerView.Adapter adapter = new PopularAdapter(list);
                        binding.recyclerViewPopular.setAdapter(adapter);
                    }
                    binding.progressBarPopular.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (isAdded() && binding != null) {
                    binding.progressBarPopular.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initRecommended() {
        DatabaseReference myRef = database.getReference("Item");
        if (binding != null) {
            binding.progressBarRecommended.setVisibility(View.VISIBLE);
        }

        ArrayList<ItemDomain> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!isAdded() || getContext() == null || binding == null) {
                    return;
                }

                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(ItemDomain.class));
                    }
                    if (!list.isEmpty()) {
                        binding.recyclerViewRecommended.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
                        RecyclerView.Adapter adapter = new RecommendedAdapter(list);
                        binding.recyclerViewRecommended.setAdapter(adapter);
                    }
                    binding.progressBarRecommended.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (isAdded() && binding != null) {
                    binding.progressBarRecommended.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initCategory() {
        DatabaseReference myRef = database.getReference("Category");
        if (binding != null) {
            binding.progressBarCategory.setVisibility(View.VISIBLE);
        }
        ArrayList<Category> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!isAdded() || getContext() == null || binding == null) {
                    return;
                }

                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Category.class));
                    }
                    if (!list.isEmpty()) {
                        binding.recyclerViewCategory.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
                        RecyclerView.Adapter adapter = new CategoryAdapter(list);
                        binding.recyclerViewCategory.setAdapter(adapter);
                    }
                    binding.progressBarCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (isAdded() && binding != null) {
                    binding.progressBarCategory.setVisibility(View.GONE);
                }
            }
        });
    }

    private void banners(ArrayList<SliderItems> items) {
        if (binding != null) {
            binding.viewPagerSlider.setAdapter(new SliderAdapter(items, binding.viewPagerSlider));
            binding.viewPagerSlider.setClipToPadding(false);
            binding.viewPagerSlider.setClipChildren(false);
            binding.viewPagerSlider.setOffscreenPageLimit(3);
            binding.viewPagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
            compositePageTransformer.addTransformer(new MarginPageTransformer(40));
            binding.viewPagerSlider.setPageTransformer(compositePageTransformer);
        }
    }

    private void initBanner() {
        DatabaseReference myRef = database.getReference("Banner");
        if (binding != null) {
            binding.progressBarBanner.setVisibility(View.VISIBLE);
        }
        ArrayList<SliderItems> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!isAdded() || getContext() == null || binding == null) {
                    return;
                }

                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(SliderItems.class));
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (isAdded() && binding != null) {
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Giải phóng binding khi Fragment bị hủy
    }
}