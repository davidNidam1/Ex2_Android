package com.example.Facybook_android.enteties;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Post {

    @PrimaryKey(autoGenerate = true)
    private String id;
    private String username;
    private String timePublished;
    private String content;
    private int likes;
    private List<Comment> comments;

    private String profilePath;

    private String postPath;
    private Drawable profilePic;

    private Drawable postPic;

    private boolean isLiked;

    public Post(String username, String content, Drawable profilePic, Drawable postPic,
                int likes, String id, String timePublished) {
        this.username = username;
        this.content = content;
        this.profilePic = profilePic;
        this.postPic = postPic;
        this.isLiked = false;
        this.likes = likes;
        this.id = id;
        this.timePublished = timePublished;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

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

    public void setProfilePic(Drawable profilePic) {
        this.profilePic = profilePic;
    }

    public Drawable getPostPic() {
        return postPic;
    }

    public Uri getPostPicUri(Context context)
    { Bitmap bitmap = DrawableUtils.drawableToBitmap(postPic);
        return DrawableUtils.bitmapToUri(context, bitmap);}

    public void setPostPic(Drawable postPic) {
        this.postPic = postPic;
    }

    public String getId() {
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

    public void setId(String id) {
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

