package com.example.secondhandbooks.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowMetrics;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.secondhandbooks.R;
import com.example.secondhandbooks.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // This block runs after the view is created and ensures it's ready for measurement.
        view.post(() -> {
            // Get the height of the status bar
            final WindowMetrics metrics = requireActivity().getWindowManager().getCurrentWindowMetrics();
            final android.graphics.Insets insets = metrics.getWindowInsets().getInsets(android.view.WindowInsets.Type.statusBars());
            int statusBarHeight = insets.top;

            // Get the current layout parameters of the back button
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.buttonBack.getLayoutParams();

            // The original top margin is 16dp. We convert it to pixels.
            int originalTopMarginInPixels = (int) (16 * getResources().getDisplayMetrics().density);

            // Add the status bar height to the existing top margin
            layoutParams.topMargin = originalTopMarginInPixels + statusBarHeight;

            // Apply the new layout parameters to the button
            binding.buttonBack.setLayoutParams(layoutParams);
        });

        final NavController navController = Navigation.findNavController(view);

        // Back button navigation
        binding.buttonBack.setOnClickListener(v -> navController.navigateUp());

        // --- START: THIS IS THE ADDED CLICK LISTENER ---
        // When the user clicks "Login", navigate to the LoginFragment.
        binding.textViewLogin.setOnClickListener(v -> {
            navController.navigate(R.id.action_registerFragment_to_loginFragment);
        });
        // --- END: THIS IS THE ADDED CLICK LISTENER ---

        // Your main "Register" button logic would go here. For example:
        // binding.buttonRegister.setOnClickListener(v -> { /* ... register user ... */ });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}








