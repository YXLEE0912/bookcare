package com.example.bookcare_qy;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class ViewBookDetailFragment extends Fragment {

    private Book book;
    private BookRepository bookRepository;

    public ViewBookDetailFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookRepository = new BookRepository();
        if (getArguments() != null) {
            book = getArguments().getParcelable("book");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_book_detail, container, false);

        ImageView backNav = view.findViewById(R.id.back_navigation_area);
        backNav.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        if (book != null) {
            TextView tvTitle = view.findViewById(R.id.book_title);
            TextView tvAuthor = view.findViewById(R.id.book_author);
            TextView tvCondition = view.findViewById(R.id.condition_tag);
            TextView tvGenres = view.findViewById(R.id.genres_tag);
            TextView tvDescription = view.findViewById(R.id.book_description);
            TextView tvOwnerName = view.findViewById(R.id.owner_name);
            TextView tvOwnerPhone = view.findViewById(R.id.owner_phone_number);
            ImageView ivOwnerProfile = view.findViewById(R.id.owner_profile_picture);
            Button btnRequest = view.findViewById(R.id.bottom_action_button);

            tvTitle.setText(book.getTitle() != null ? book.getTitle() : "Unknown Title");
            tvAuthor.setText(book.getAuthor() != null ? "by " + book.getAuthor() : "by Unknown Author");

            String conditionText = book.getCondition() != null && !book.getCondition().isEmpty()
                    ? book.getCondition() : "Not specified";
            tvCondition.setText(conditionText);

            if (book.getGenre() != null && !book.getGenre().isEmpty()) {
                tvGenres.setText(book.getGenre());
            } else {
                tvGenres.setVisibility(View.GONE);
            }

            // Display description from user input
            String description = book.getDescription() != null && !book.getDescription().isEmpty()
                    ? book.getDescription() : "No description provided.";
            tvDescription.setText(description);
            
            // Load owner information from Firebase
            loadOwnerInformation(tvOwnerName, tvOwnerPhone, ivOwnerProfile, book.getOwnerId());

            if ("Donation".equalsIgnoreCase(book.getListingType())) {
                btnRequest.setText("Request Donation");
            } else {
                btnRequest.setText("Request Exchange");
            }

            btnRequest.setOnClickListener(v -> {
                if ("Exchange".equalsIgnoreCase(book.getListingType())) {
                    handleExchangeRequest();
                } else {
                    handleDonationRequest();
                }
            });
        }

        return view;
    }

    private void loadOwnerInformation(TextView tvOwnerName, TextView tvOwnerPhone, ImageView ivOwnerProfile, String ownerId) {
        if (ownerId == null || ownerId.isEmpty()) {
            tvOwnerName.setText("Unknown User");
            tvOwnerPhone.setText("N/A");
            ivOwnerProfile.setImageResource(R.drawable.ic_account_circle);
            return;
        }
        
        DatabaseReference userRef = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL)
                .getReference(Constants.PATH_USERS)
                .child(ownerId);
        
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User owner = snapshot.getValue(User.class);
                if (owner != null) {
                    tvOwnerName.setText(owner.getUsername() != null ? owner.getUsername() : "Unknown User");
                    tvOwnerPhone.setText(owner.getPhone() != null ? owner.getPhone() : "N/A");
                    
                    // Load profile picture
                    if (owner.getProfilePictureUrl() != null && !owner.getProfilePictureUrl().isEmpty()) {
                        Glide.with(requireContext())
                                .load(owner.getProfilePictureUrl())
                                .placeholder(R.drawable.ic_account_circle)
                                .error(R.drawable.ic_account_circle)
                                .circleCrop()
                                .into(ivOwnerProfile);
                    } else {
                        ivOwnerProfile.setImageResource(R.drawable.ic_account_circle);
                    }
                } else {
                    tvOwnerName.setText("Unknown User");
                    tvOwnerPhone.setText("N/A");
                    ivOwnerProfile.setImageResource(R.drawable.ic_account_circle);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tvOwnerName.setText("Unknown User");
                tvOwnerPhone.setText("N/A");
                ivOwnerProfile.setImageResource(R.drawable.ic_account_circle);
            }
        });
    }

    private void handleExchangeRequest() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(requireContext(), "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Check credits from Firebase
        DatabaseReference userRef = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL)
                .getReference(Constants.PATH_USERS)
                .child(currentUser.getUid())
                .child("credits");
        
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Integer currentCredits = task.getResult().getValue(Integer.class);
                if (currentCredits == null) currentCredits = 0;
                
                if (currentCredits >= 1) {
                    // Deduct credit and proceed
                    userRef.setValue(currentCredits - 1);
                    
                    // Remove book from all lists after successful exchange
                    if (book != null && book.getId() != null) {
                        bookRepository.deleteBook(book.getId());
                    }
                    showExchangeDoneDialog();
                } else {
                    showInsufficientCreditsDialog(currentCredits);
                }
            } else {
                showInsufficientCreditsDialog(0);
            }
        });
    }

    private void handleDonationRequest() {
        // Remove book from all lists after successful donation
        if (book != null && book.getId() != null) {
            bookRepository.deleteBook(book.getId());
        }
        showDonationDoneDialog();
    }
    
    private void showDonationDoneDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_request_sent_pop_up);

        Window window = dialog.getWindow();
        if (window != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        View btnClose = dialog.findViewById(R.id.btn_close);
        TextView tvTitle = dialog.findViewById(R.id.text_title);
        TextView tvMessage1 = dialog.findViewById(R.id.text_message_line1);
        TextView tvMessage2 = dialog.findViewById(R.id.text_message_line2);
        TextView tvPhone = dialog.findViewById(R.id.text_phone_number);

        tvTitle.setText("Donation Claimed");
        tvMessage1.setText("You claim this book.");
        tvMessage2.setText("For more information, please contact the owner at:");
        // Load phone from book owner
        if (book != null && book.getOwnerId() != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL)
                    .getReference(Constants.PATH_USERS)
                    .child(book.getOwnerId());
            userRef.child("phone").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String phone = snapshot.getValue(String.class);
                    tvPhone.setText(phone != null ? phone : "N/A");
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    tvPhone.setText("N/A");
                }
            });
        } else {
            tvPhone.setText("N/A");
        }

        btnClose.setOnClickListener(v -> {
            dialog.dismiss();
            Navigation.findNavController(requireView()).navigateUp();
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
    
    private void logTransaction(int points, String type) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            PointTransaction transaction = new PointTransaction(userId, points, type, new Date());
            bookRepository.addPointTransaction(transaction);
            
            // Update user's total points and badge level
            DatabaseReference userRef = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL)
                    .getReference(Constants.PATH_USERS)
                    .child(userId);
            
            userRef.child("totalPoints").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Integer currentPoints = task.getResult().getValue(Integer.class);
                    if (currentPoints == null) currentPoints = 0;
                    int newPoints = currentPoints + points;
                    userRef.child("totalPoints").setValue(newPoints);
                    
                    // Update badge level (50 points per level, starting at level 1)
                    // Level 1 = 0-49 points, Level 2 = 50-99 points, etc.
                    int newBadgeLevel;
                    if (newPoints == 0) {
                        newBadgeLevel = 0; // No level yet
                    } else {
                        newBadgeLevel = (newPoints / 50) + 1;
                        if (newBadgeLevel > 50) newBadgeLevel = 50; // Max level is 50
                    }
                    userRef.child("badgeLevel").setValue(newBadgeLevel);
                }
            });
        }
    }

    private void showInsufficientCreditsDialog(int currentCredits) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_request_sent_pop_up);

        Window window = dialog.getWindow();
        if (window != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        View btnClose = dialog.findViewById(R.id.btn_close);
        TextView tvTitle = dialog.findViewById(R.id.text_title);
        TextView tvMessage = dialog.findViewById(R.id.text_message_line1);
        tvTitle.setText("Insufficient Credits");
        tvMessage.setText("You need 1 credit to request an exchange. You currently have " +
                currentCredits + " credit(s). Add a book for exchange to earn credits!");

        dialog.findViewById(R.id.text_message_line2).setVisibility(View.GONE);
        dialog.findViewById(R.id.phone_layout).setVisibility(View.GONE);

        btnClose.setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void showExchangeDoneDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_request_sent_pop_up);

        Window window = dialog.getWindow();
        if (window != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        View btnClose = dialog.findViewById(R.id.btn_close);
        TextView tvTitle = dialog.findViewById(R.id.text_title);
        TextView tvMessage1 = dialog.findViewById(R.id.text_message_line1);
        TextView tvMessage2 = dialog.findViewById(R.id.text_message_line2);
        TextView tvPhone = dialog.findViewById(R.id.text_phone_number);

        tvTitle.setText("Exchange Done");
        tvMessage1.setText("This exchange will post to the forum.");
        tvMessage2.setText("For more information, \nplease contact the owner at:");
        // Load phone from book owner
        if (book != null && book.getOwnerId() != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL)
                    .getReference(Constants.PATH_USERS)
                    .child(book.getOwnerId());
            userRef.child("phone").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String phone = snapshot.getValue(String.class);
                    tvPhone.setText(phone != null ? phone : "N/A");
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    tvPhone.setText("N/A");
                }
            });
        } else {
            tvPhone.setText("N/A");
        }

        btnClose.setOnClickListener(v -> {
            dialog.dismiss();
            Navigation.findNavController(requireView()).navigateUp();
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
