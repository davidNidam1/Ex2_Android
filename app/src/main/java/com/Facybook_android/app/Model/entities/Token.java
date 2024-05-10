package com.Facybook_android.app.Model.entities;

import java.util.Date;

public class Token {
    private String token;
    private Date createdAt;

    // Getter and setter for token
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // Getter and setter for createdAt
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

