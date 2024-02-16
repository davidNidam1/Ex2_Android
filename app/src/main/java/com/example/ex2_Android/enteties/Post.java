package com.example.ex2_Android.enteties;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.ex2_android.R;

@Entity
public class Post {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String timePublished;
    private String content;

    private int likes;

    private int pic;

    public Post() {
        this.pic = R.drawable.user_icon;
    }

    public Post(String username, String content, int pic) {
        this.username = username;
        this.content = content;
        this.pic = pic;
    }

    public int getLikes() {
        return likes;
    }

    public int getPic() {
        return pic;
    }

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

    public void setPic(int pic) {
        this.pic = pic;
    }
}

