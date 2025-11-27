package com.example.secondhandbooks.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.secondhandbooks.R;
import com.example.secondhandbooks.databinding.FragmentSettingsBinding;
import com.example.secondhandbooks.viewmodel.SharedViewModel;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private SharedViewModel sharedViewModel;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE);

        binding.setViewModel(sharedViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        // Your existing navigation logic...
        binding.rowEditProfile.setOnClickListener(v -> navController.navigate(R.id.action_navigation_settings_to_profileEditFragment));
        binding.cardUserProfile.setOnClickListener(v -> navController.navigate(R.id.action_navigation_settings_to_profileViewFragment));
        binding.rowLogout.setOnClickListener(v -> navController.navigate(R.id.action_global_logout));

        // --- START: BLUE MODE LOGIC ---
        // 1. Set the switch to the correct initial state.
        boolean isBlueMode = sharedPreferences.getBoolean("isBlueMode", false);
        binding.switchBlueMode.setChecked(isBlueMode);

        // 2. Add a listener to the switch.
        binding.switchBlueMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isBlueMode", isChecked);
            editor.apply();
            // Recreate the activity to apply the new theme.
            requireActivity().recreate();
        });
        // --- END: BLUE MODE LOGIC ---
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}














