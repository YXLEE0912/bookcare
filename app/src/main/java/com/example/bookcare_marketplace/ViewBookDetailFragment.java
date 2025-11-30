package com.example.bookcare_marketplace;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

// Note: Replace 'com.example.yourapp' with your actual package name

public class ViewBookDetailFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // This line is the most important: it tells the Fragment to load
        // the design defined in res/layout/book_view_details.xml
        return inflater.inflate(R.layout.fragment_view_book_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Setup the Back/Undo Button Listener
        setupBackNavigation(view);

        // 2. Setup the "Request Exchange" Button Listener
        setupActionButton(view);

        // TODO: Data fetching and UI updating goes here (e.g., getting book details
        // from arguments and setting TextViews)
    }

    private void setupBackNavigation(View view) {
        // Find the clickable LinearLayout we defined in the XML
        LinearLayout backNavigationArea = view.findViewById(R.id.back_navigation_area);
        backNavigationArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Attempt to navigate back using the Navigation Component (modern approach)
                try {
                    // Check if the fragment can navigate up the navigation stack
                    NavHostFragment.findNavController(ViewBookDetailFragment.this).popBackStack();
                } catch (IllegalStateException e) {
                    // Fallback: use standard activity back press if Navigation Component is not running
                    requireActivity().onBackPressed();
                }
            }
        });
    }

    private void setupActionButton(View view) {
        // Find the main action button
        Button actionButton = view.findViewById(R.id.bottom_action_button);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display a temporary message to confirm the button works
                Toast.makeText(getContext(), "Exchange Request Initiated!", Toast.LENGTH_SHORT).show();
                // TODO: Add logic for starting the exchange process here
            }
        });
    }
}
