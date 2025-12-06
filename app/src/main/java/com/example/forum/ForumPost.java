package com.example.forum;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class ForumPost {

    private String postId;
    private String posterId;
    private String posterName;
    private String postType;
    private String title;
    private String message;
    private Date timestamp;
    private int upvotes;
    private int commentCount;
    private boolean likedByCurrentUser = false;

    // Required empty public constructor for Firestore
    public ForumPost() {}

    public ForumPost(String posterId, String posterName, String postType, String title, String message) {
        this.posterId = posterId;
        this.posterName = posterName;
        this.postType = postType;
        this.title = title;
        this.message = message;
        this.upvotes = 0;
        this.commentCount = 0;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPosterId() {
        return posterId;
    }

    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @ServerTimestamp
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        this.likedByCurrentUser = likedByCurrentUser;
    }
}
