package com.example.bookcare;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FirebaseTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            // Initialize Firebase (optional, usually auto)
            FirebaseApp.initializeApp(this);

            // Get FirebaseAuth instance
            FirebaseAuth auth = FirebaseAuth.getInstance();
            Log.d(TAG, "FirebaseAuth initialized: " + auth);

        } catch (Exception e) {
            Log.e(TAG, "Firebase failed to initialize", e);
        }
    }
}
