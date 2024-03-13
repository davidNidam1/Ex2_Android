package com.example.Facybook_android.Model.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.Facybook_android.Model.Converters;

import java.util.List;

@Entity
public class Post {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String timePublished;
    private String content;
    private int likes;
//    private List<Comment> comments;
    private String profilePath;
    private String postPath;
    private Drawable profilePic;
    private boolean isLiked;
    private long creationTime;

    public Post(String username, String content, Drawable profilePic, int likes,
                String timePublished, String postPath) {
        this.username = username;
        this.content = content;
        this.profilePic = profilePic;
        this.isLiked = false;
        this.likes = likes;
        this.timePublished = timePublished;
        this.postPath = postPath;
        this.creationTime = System.currentTimeMillis(); // Set the creation time to current time
    }

    // Getter and setter methods for the creation time
    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }
//    public List<Comment> getComments() {
//        return comments;
//    }
//
//    public void setComments(List<Comment> comments) {
//        this.comments = comments;
//    }

    public String getLikesString() {
        return likes + " " + "likes";
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getPostPath() {
        return postPath;
    }

    public void setPostPath(String postPath) {
        this.postPath = postPath;
    }

    public int getLikes() {
        return likes;
    }

    public Drawable getProfilePic() {
        return profilePic;
    }

//    public void setProfilePic(Drawable profilePic) {
//        this.profilePic = profilePic;
//    }

//    public Drawable getPostPic() {
//        return postPic;
//    }

//    public void setPostPic(Drawable postPic) {
//        this.postPic = postPic;
//    }


    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getTimePublished() {
        return timePublished;
    }

    public String getContent() {
        return content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTimePublished(String timePublished) {
        this.timePublished = timePublished;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

}

