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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.secondhandbooks.R;
import com.example.secondhandbooks.databinding.FragmentUserProfileBinding;
import com.example.secondhandbooks.viewmodel.SharedViewModel;

public class UserProfileFragment extends Fragment {

    private FragmentUserProfileBinding binding;
    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        binding.setViewModel(sharedViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Handle edge-to-edge insets by adding padding to the root ScrollView.
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });

        final NavController navController = Navigation.findNavController(view);

        // --- START: THIS IS THE FIX ---
        // The main profile card is no longer clickable, so that listener is removed.
        // We only need the click listener for the Eco-Points card.
        binding.cardEcoPoints.setOnClickListener(v ->
                navController.navigate(R.id.action_navigation_home_to_ecoPointsFragment)
        );
        // --- END: THIS IS THE FIX ---
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}





