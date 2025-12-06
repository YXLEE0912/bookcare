package com.example.forum;

import android.content.Context;
import android.widget.Toast;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirebaseForumService {

    private final FirebaseFirestore db;
    private final Context context;
    private final CollectionReference postsCollection;

    public interface ForumCallback<T> {
        void onCallback(T data);
    }

    public FirebaseForumService(Context context) {
        this.db = FirebaseFirestore.getInstance();
        this.context = context;
        this.postsCollection = db.collection("posts");
    }

    public void getPosts(ForumCallback<List<ForumPost>> callback) {
        postsCollection.orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Toast.makeText(context, "Error loading posts: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<ForumPost> posts = new ArrayList<>();
                    if (snapshots != null) {
                        for (QueryDocumentSnapshot doc : snapshots) {
                            ForumPost post = doc.toObject(ForumPost.class);
                            post.setPostId(doc.getId());
                            posts.add(post);
                        }
                    }
                    callback.onCallback(posts);
                });
    }

    public void addPost(ForumPost post) {
        postsCollection.add(post)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(context, "Post created!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error creating post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void toggleLike(String postId, String userId) {
        DocumentReference postRef = postsCollection.document(postId);
        postRef.collection("likes").document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    postRef.collection("likes").document(userId).delete();
                    postRef.update("upvotes", FieldValue.increment(-1));
                } else {
                    postRef.collection("likes").document(userId).set(new Like(userId));
                    postRef.update("upvotes", FieldValue.increment(1));
                }
            }
        });
    }

    public void addComment(String postId, Comment comment) {
        postsCollection.document(postId).collection("comments").add(comment);
        postsCollection.document(postId).update("commentCount", FieldValue.increment(1));
    }

    public void getLikes(String postId, ForumCallback<List<Like>> callback) {
        postsCollection.document(postId).collection("likes")
                .addSnapshotListener((snapshots, e) -> {
                    List<Like> likes = new ArrayList<>();
                    if (snapshots != null) {
                        for (QueryDocumentSnapshot doc : snapshots) {
                            likes.add(doc.toObject(Like.class));
                        }
                    }
                    callback.onCallback(likes);
                });
    }

    public void getComments(String postId, ForumCallback<List<Comment>> callback) {
        postsCollection.document(postId).collection("comments")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    List<Comment> comments = new ArrayList<>();
                    if (snapshots != null) {
                        for (QueryDocumentSnapshot doc : snapshots) {
                            comments.add(doc.toObject(Comment.class));
                        }
                    }
                    callback.onCallback(comments);
                });
    }
}