package com.example.forum;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class Comment {

    private String userId;
    private String text;
    private Date timestamp;

    // Required empty public constructor for Firestore
    public Comment() {}

    public Comment(String userId, String text) {
        this.userId = userId;
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @ServerTimestamp
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
