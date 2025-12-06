package com.example.forum;

public class Like {

    private String userId;

    // Required empty public constructor for Firestore
    public Like() {}

    public Like(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
