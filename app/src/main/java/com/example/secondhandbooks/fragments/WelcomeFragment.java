package com.example.secondhandbooks.fragments;

import android.os.Bundle;
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
import com.example.secondhandbooks.R;
import com.example.secondhandbooks.databinding.FragmentWelcomeBinding;

public class WelcomeFragment extends Fragment {

    private FragmentWelcomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // This listener will add the necessary padding to the root layout to avoid the system bars.
        ViewCompat.setOnApplyWindowInsetsListener(binding.welcomeLayout, (v, insets) -> {
            // Get the insets for the system bars (status bar, navigation bar)
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Apply the insets as padding to the root view
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            // Return the insets so the system can continue processing them
            return insets;
        });

        // Set up navigation
        final NavController navController = Navigation.findNavController(view);
        binding.buttonLogin.setOnClickListener(v -> {
            // Navigate to the Login page
            navController.navigate(R.id.action_welcomeFragment_to_loginFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

