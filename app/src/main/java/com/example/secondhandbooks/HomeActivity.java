package com.example.secondhandbooks;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View; // --- START: THIS IS THE FIX - Ensure this correct import is here ---
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.example.secondhandbooks.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // --- THEME APPLICATION LOGIC ---
        SharedPreferences sharedPreferences = getSharedPreferences("ThemePrefs", MODE_PRIVATE);
        boolean isBlueMode = sharedPreferences.getBoolean("isBlueMode", false);

        if (isBlueMode) {
            setTheme(R.style.Theme_SecondhandBooks_Blue);
        } else {
            setTheme(R.style.Theme_SecondhandBooks);
        }
        // --- END THEME LOGIC ---

        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_home);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Your logic to hide/show the bottom nav bar...
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.welcomeFragment || destination.getId() == R.id.loginFragment ||
                    destination.getId() == R.id.registerFragment || destination.getId() == R.id.forgotPasswordFragment) {
                // --- START: THIS IS THE FIX - Use the correct View class ---
                binding.navView.setVisibility(View.GONE);
            } else {
                binding.navView.setVisibility(View.VISIBLE);
                // --- END: THIS IS THE FIX ---
            }
        });
    }
}






