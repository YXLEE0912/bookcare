package com.example.forum;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ForumActivity extends AppCompatActivity implements PostAdapter.OnPostInteractionListener, CommentDialogFragment.OnCommentAddedListener {

    private String currentUserId = "user123";
    private RecyclerView rvPosts;
    private FloatingActionButton fabNewPost;
    private PostAdapter postAdapter;
    private List<ForumPost> postsList;
    private FirebaseForumService firebaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        // The order of these calls is important. We need the service before the adapter.
        setupFirebase();
        setupViews();
        loadPosts();
    }

    private void setupViews() {
        rvPosts = findViewById(R.id.rvPosts);
        fabNewPost = findViewById(R.id.fabNewPost);

        postsList = new ArrayList<>();
        postAdapter = new PostAdapter(postsList, this, firebaseService);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        rvPosts.setAdapter(postAdapter);

        fabNewPost.setOnClickListener(v -> showCreatePostDialog());
    }

    private void setupFirebase() {
        firebaseService = new FirebaseForumService(this);
    }

    private void loadPosts() {
        firebaseService.getPosts(posts -> {
            postsList.clear();
            postsList.addAll(posts);
            postAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onLikeClicked(ForumPost post, int position) {
        firebaseService.toggleLike(post.getPostId(), currentUserId);
    }

    @Override
    public void onCommentClicked(ForumPost post) {
        CommentDialogFragment.newInstance(post.getPostId()).show(getSupportFragmentManager(), "CommentDialog");
    }

    @Override
    public void onCommentAdded(String postId, String commentText) {
        Comment newComment = new Comment(currentUserId, commentText);
        firebaseService.addComment(postId, newComment);
    }

    private void showCreatePostDialog() {
        CreatePostDialog dialog = new CreatePostDialog();
        dialog.show(getSupportFragmentManager(), "CreatePostDialog");
    }

    public void createNewPost(String postType, String title, String message) {
        ForumPost newPost = new ForumPost(
                currentUserId,
                "Current User", // This will be replaced with the actual user's name
                postType,
                title,
                message
        );
        firebaseService.addPost(newPost);
    }
}
