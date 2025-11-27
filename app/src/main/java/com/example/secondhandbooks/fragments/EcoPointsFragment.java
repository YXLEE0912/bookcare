package com.example.secondhandbooks.fragments;import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.secondhandbooks.databinding.FragmentEcoPointsBinding;

public class EcoPointsFragment extends Fragment {

    private FragmentEcoPointsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Use View Binding to inflate the layout
        binding = FragmentEcoPointsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // This listener adds the necessary padding to the root layout to avoid the system bars.
        ViewCompat.setOnApplyWindowInsetsListener(binding.ecoPointsLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find the NavController
        final NavController navController = Navigation.findNavController(view);

        // Set the click listener for our new arrow button
        binding.buttonBack.setOnClickListener(v -> {
            // This tells the NavController to simply go back to the previous screen (Home page).
            navController.navigateUp();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}



