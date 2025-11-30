package com.example.bookcare_marketplace;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

// Note: Replace 'com.example.yourapp' with your actual package name

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This line links the Activity to the layout we defined in Step 1
        setContentView(R.layout.activity_main);

        // Check if the activity is being restored from a previous state.
        // We only want to add the fragment on the first creation.
        if (savedInstanceState == null) {

            // 1. Get the FragmentManager
            // (Use getSupportFragmentManager() because we extend AppCompatActivity)

            // 2. Start a transaction (a set of steps to add/replace/remove fragments)
            getSupportFragmentManager().beginTransaction()

                    // 3. Replace the FrameLayout (fragment_container) with your new Fragment
                    .replace(R.id.fragment_container, new SearchBookFragment())

                    // 4. Commit the transaction (execute the changes)
                    .commit();
        }
    }
}
