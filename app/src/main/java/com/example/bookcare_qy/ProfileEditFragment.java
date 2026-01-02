package com.example.bookcare_qy;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bookcare_qy.databinding.FragmentProfileEditBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.Toast;

public class ProfileEditFragment extends Fragment {

    private FragmentProfileEditBinding binding;
    private SharedViewModel sharedViewModel;

    private final ActivityResultLauncher<String> getContentLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    sharedViewModel.profileImageUri.setValue(uri);
                }
            });

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentProfileEditBinding.inflate(inflater, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        binding.setViewModel(sharedViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        // ✅ FIXED: Back arrow position (compatible with all Android versions)
        view.post(() -> {
            int statusBarHeight = 0;
            int resourceId = getResources().getIdentifier(
                    "status_bar_height", "dimen", "android"
            );
            if (resourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }

            ConstraintLayout.LayoutParams layoutParams =
                    (ConstraintLayout.LayoutParams) binding.buttonBack.getLayoutParams();

            int originalTopMarginInPixels =
                    (int) (16 * getResources().getDisplayMetrics().density);

            layoutParams.topMargin = originalTopMarginInPixels + statusBarHeight;
            binding.buttonBack.setLayoutParams(layoutParams);
        });

        NavController navController = Navigation.findNavController(view);

        // Back button
        binding.buttonBack.setOnClickListener(v -> navController.navigateUp());

        // Pick profile image
        binding.imageViewProfile.setOnClickListener(v ->
                getContentLauncher.launch("image/*")
        );
        
        // Make profile image circular
        view.post(() -> {
            binding.imageViewProfile.setClipToOutline(true);
            binding.imageViewProfile.setOutlineProvider(new android.view.ViewOutlineProvider() {
                @Override
                public void getOutline(android.view.View view, android.graphics.Outline outline) {
                    outline.setOval(0, 0, view.getWidth(), view.getHeight());
                }
            });
        });

        // Load user data
        loadUserDataFromFirebase();

        // Setup genre spinner
        Spinner genreSpinner = binding.getRoot().findViewById(R.id.spinnerGenre);
        if (genreSpinner != null) {
            genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedGenre = parent.getItemAtPosition(position).toString();
                    sharedViewModel.genrePreference.setValue(selectedGenre);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        // Save button
        binding.buttonSave.setOnClickListener(v -> {
            saveUserProfileToFirebase(navController);
        });
    }

    private void saveUserProfileToFirebase(NavController navController) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        DatabaseReference userRef =
                FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL)
                        .getReference(Constants.PATH_USERS)
                        .child(currentUser.getUid());

        String name = sharedViewModel.name.getValue();
        String age = sharedViewModel.age.getValue();
        String bio = sharedViewModel.bio.getValue();
        String phone = sharedViewModel.phone.getValue();
        String location = sharedViewModel.location.getValue();
        String genrePreference = sharedViewModel.genrePreference.getValue();

        // Save text fields first
        if (name != null) userRef.child("username").setValue(name);
        if (age != null) userRef.child("age").setValue(age);
        if (bio != null) userRef.child("bio").setValue(bio);
        if (phone != null) userRef.child("phone").setValue(phone);
        if (location != null) userRef.child("address").setValue(location);
        if (genrePreference != null) userRef.child("genrePreference").setValue(genrePreference);

        // Upload profile picture if one was selected
        Uri profileImageUri = sharedViewModel.profileImageUri.getValue();
        if (profileImageUri != null) {
            uploadProfilePicture(profileImageUri, currentUser.getUid(), userRef, navController);
        } else {
            navController.navigateUp();
        }
    }

    private void uploadProfilePicture(Uri imageUri, String userId, DatabaseReference userRef, NavController navController) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference profileImagesRef = storageRef.child("profile_pictures/" + userId + ".jpg");

        UploadTask uploadTask = profileImagesRef.putFile(imageUri);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Get download URL
            profileImagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Save URL to user profile
                userRef.child("profilePictureUrl").setValue(uri.toString())
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(requireContext(), "Profile saved successfully!", Toast.LENGTH_SHORT).show();
                            navController.navigateUp();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(requireContext(), "Failed to save profile picture URL", Toast.LENGTH_SHORT).show();
                            navController.navigateUp();
                        });
            }).addOnFailureListener(e -> {
                Toast.makeText(requireContext(), "Failed to get image URL", Toast.LENGTH_SHORT).show();
                navController.navigateUp();
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(requireContext(), "Failed to upload profile picture", Toast.LENGTH_SHORT).show();
            navController.navigateUp();
        });
    }

    private void loadUserDataFromFirebase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        DatabaseReference userRef =
                FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL)
                        .getReference(Constants.PATH_USERS)
                        .child(currentUser.getUid());

        userRef.addListenerForSingleValueEvent(
                new com.google.firebase.database.ValueEventListener() {

                    @Override
                    public void onDataChange(
                            @NonNull com.google.firebase.database.DataSnapshot snapshot
                    ) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            if (user.getUsername() != null)
                                sharedViewModel.name.setValue(user.getUsername());
                            if (user.getEmail() != null)
                                sharedViewModel.email.setValue(user.getEmail());
                            if (user.getPhone() != null)
                                sharedViewModel.phone.setValue(user.getPhone());
                            if (user.getAddress() != null)
                                sharedViewModel.location.setValue(user.getAddress());
                            if (user.getAge() != null)
                                sharedViewModel.age.setValue(user.getAge());
                            if (user.getBio() != null)
                                sharedViewModel.bio.setValue(user.getBio());
                            if (user.getGenrePreference() != null) {
                                sharedViewModel.genrePreference.setValue(user.getGenrePreference());
                                // Set spinner selection
                                Spinner genreSpinner = binding.getRoot().findViewById(R.id.spinnerGenre);
                                if (genreSpinner != null) {
                                    android.content.res.Resources res = getResources();
                                    String[] genres = res.getStringArray(R.array.book_genre);
                                    for (int i = 0; i < genres.length; i++) {
                                        if (genres[i].equals(user.getGenrePreference())) {
                                            genreSpinner.setSelection(i);
                                            break;
                                        }
                                    }
                                }
                            }
                            // Load profile picture URL if available
                            if (user.getProfilePictureUrl() != null && !user.getProfilePictureUrl().isEmpty()) {
                                sharedViewModel.profileImageUri.setValue(Uri.parse(user.getProfilePictureUrl()));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(
                            @NonNull com.google.firebase.database.DatabaseError error
                    ) {
                        // Handle error if needed
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
