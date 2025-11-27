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
import com.example.secondhandbooks.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Adjust back button for status bar height
        view.post(() -> {
            final WindowMetrics metrics = requireActivity().getWindowManager().getCurrentWindowMetrics();
            final android.graphics.Insets insets = metrics.getWindowInsets().getInsets(android.view.WindowInsets.Type.systemBars());
            int statusBarHeight = insets.top;
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.buttonBack.getLayoutParams();
            int originalTopMarginInPixels = (int) (16 * getResources().getDisplayMetrics().density);
            layoutParams.topMargin = originalTopMarginInPixels + statusBarHeight;
            binding.buttonBack.setLayoutParams(layoutParams);
        });

        final NavController navController = Navigation.findNavController(view);

        // Navigation for Back, Register, and Forgot Password
        binding.buttonBack.setOnClickListener(v -> navController.navigateUp());
        binding.textViewRegister.setOnClickListener(v -> navController.navigate(R.id.action_loginFragment_to_registerFragment));
        binding.textViewForgotPassword.setOnClickListener(v -> navController.navigate(R.id.action_loginFragment_to_forgotPasswordFragment));

        // Navigation for the main Login button
        binding.buttonLogin.setOnClickListener(v -> {
            // Add your real login validation here.
            // If login is successful, navigate to the home graph.
            navController.navigate(R.id.action_loginFragment_to_home_nav_graph);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}








