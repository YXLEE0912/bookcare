package com.example.bookcare;

import android.content.Context;
import android.widget.Toast;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class FirebaseForumService {
    private DatabaseReference databaseReference;
    private Context context;

    public FirebaseForumService(Context context) {
        this.context = context;
        setupFirebase();
    }

    private void setupFirebase() {
        try {
            databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        } catch (Exception e) {
            showToast("Firebase setup failed: " + e.getMessage());
        }
    }

    // Add a new post to Firebase
    public void addPostToFirebase(ForumPost post) {
        try {
            // Generate a unique key for the post
            String postId = databaseReference.push().getKey();
            if (postId != null) {
                post.setId(postId); // Set the generated ID to the post
                databaseReference.child(postId).setValue(post)
                        .addOnSuccessListener(aVoid -> {
                            showToast("Posted successfully!");
                        })
                        .addOnFailureListener(e -> {
                            showToast("Post failed: " + e.getMessage());
                        });
            } else {
                showToast("Post failed: Could not generate post ID");
            }
        } catch (Exception e) {
            showToast("Post failed: " + e.getMessage());
        }
    }

    // Get all posts from Firebase
    public void getPostsFromFirebase(PostsCallback callback) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ForumPost> posts = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ForumPost post = postSnapshot.getValue(ForumPost.class);
                    if (post != null) {
                        posts.add(post);
                    }
                }
                callback.onPostsLoaded(posts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showToast("Failed to load posts: " + databaseError.getMessage());
                callback.onPostsLoaded(new ArrayList<>());
            }
        });
    }

    // Update an existing post
    public void updatePostInFirebase(ForumPost post) {
        if (post.getId() != null) {
            databaseReference.child(post.getId()).setValue(post)
                    .addOnSuccessListener(aVoid -> {
                        showToast("Post updated successfully!");
                    })
                    .addOnFailureListener(e -> {
                        showToast("Update failed33: " + e.getMessage());
                    });
        } else {
            showToast("Update failed: Post ID is null");
        }
    }

    // Delete a post
    public void deletePostFromFirebase(String postId) {
        if (postId != null) {
            databaseReference.child(postId).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        showToast("Post deleted successfully!");
                    })
                    .addOnFailureListener(e -> {
                        showToast("Delete failed: " + e.getMessage());
                    });
        } else {
            showToast("Delete failed: Post ID is null");
        }
    }

    // Helper method to show Toast
    private void showToast(String message) {
        // Since Firebase callbacks run on main thread, we can show Toast directly
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    // Callback interface for async post loading
    public interface PostsCallback {
        void onPostsLoaded(List<ForumPost> posts);
    }
}
