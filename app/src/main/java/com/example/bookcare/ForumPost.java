package com.example.bookcare;

import java.util.Date;
import java.util.UUID;

public class ForumPost {
    private String id;
    private String posterId;
    private String posterName;
    private String title;
    private String message;
    private long timestamp;
    private int upvotes;
    private int commentCount;

    // Empty constructor (required for Firebase deserialization)
    public ForumPost() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.upvotes = 0;
        this.commentCount = 0;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
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

    @Override
    public String toString() {
        return "ForumPost{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", posterName='" + posterName + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}