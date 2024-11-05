package com.smartherd.manga2;

public class User {
    private String userId;
    private String email;
    private String username;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userId, String email, String username) {
        this.userId = userId;
        this.email = email;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}

