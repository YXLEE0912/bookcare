package com.example.bookcare;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.List;

public class ForumActivity extends AppCompatActivity implements CreatePostDialog.CreatePostListener {
    private RecyclerView rvPosts;
    private FloatingActionButton fabNewPost;
    private PostAdapter postAdapter;
    private List<ForumPost> postsList;
    private FirebaseForumService firebaseService;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        setupViews();
        setupFirebase();
        loadPosts();
    }

    private void setupViews() {
        rvPosts = findViewById(R.id.rvPosts);
        fabNewPost = findViewById(R.id.fabNewPost);

        postsList = new ArrayList<>();
        postAdapter = new PostAdapter(postsList);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        rvPosts.setAdapter(postAdapter);

        fabNewPost.setOnClickListener(v -> showCreatePostDialog());
    }

    private void setupFirebase() {
        firebaseService = new FirebaseForumService(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void loadPosts() {
        firebaseService.getPostsFromFirebase(new FirebaseForumService.PostsCallback() {
            @Override
            public void onPostsLoaded(List<ForumPost> posts) {
                postsList.clear();
                posts.sort((p1, p2) -> Long.compare(p2.getTimestamp(), p1.getTimestamp()));

                postsList.addAll(posts);
                postAdapter.notifyDataSetChanged();

                if (postsList.isEmpty()) {
                    Toast.makeText(ForumActivity.this, "No posts yet. Be the first to post!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showCreatePostDialog() {
        // Removed the sign-in check - allow dialog to open without sign-in
        CreatePostDialog dialog = new CreatePostDialog();
        dialog.show(getSupportFragmentManager(), "CreatePostDialog");
    }

    // 修复 bug：message 存到 ForumPost.message，commentCount 初始化为 0
    @Override
    public void onCreatePost(String title, String message) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        ForumPost newPost = new ForumPost();
        newPost.setTitle(title);
        newPost.setMessage(message);
        newPost.setPosterName(getCurrentUserName(currentUser));
        newPost.setPosterId(currentUser != null ? currentUser.getUid() : "anonymous");
        newPost.setTimestamp(System.currentTimeMillis());
        newPost.setCommentCount(0);            // 初始化评论数为 0

        firebaseService.addPostToFirebase(newPost);

        // ADD THIS LINE TO REFRESH POSTS AFTER CREATING
        loadPosts();

        Toast.makeText(this, "Post created successfully!", Toast.LENGTH_SHORT).show();
    }

    private String getCurrentUserName(FirebaseUser user) {
        if (user != null && user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
            return user.getDisplayName();
        }

        if (user != null && user.getEmail() != null) {
            return user.getEmail().split("@")[0]; // Get username from email
        }

        return "Anonymous User";
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ensure we're listening for post updates when activity is visible
        loadPosts();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Optional: Remove Firebase listeners here if you add that capability to FirebaseForumService
    }
}
