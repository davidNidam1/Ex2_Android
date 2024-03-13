package com.example.Facybook_android.Model.entities;

import android.graphics.drawable.Drawable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Comment {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Drawable pic;
    private String content;
    private String userName;

    public Comment(Drawable pic, String content, String userName) {
        this.pic = pic;
        this.content = content;
        this.userName = userName;
    }

    public Drawable getPic() {
        return pic;
    }

    public String getUserName() {
        return userName;
    }

    public Drawable getProfilePic() {
        return pic;
    }

    public void setPic(Drawable pic) {
        this.pic = pic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
