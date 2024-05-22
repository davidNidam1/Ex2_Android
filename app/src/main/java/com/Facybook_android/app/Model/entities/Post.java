    package com.Facybook_android.app.Model.entities;

    import android.graphics.drawable.Drawable;

    import androidx.annotation.NonNull;
    import androidx.room.Entity;
    import androidx.room.PrimaryKey;

    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;
    import java.util.UUID;

    @Entity
    public class Post {
        @PrimaryKey @NonNull
        private String pid;
        private String publisher;
        private Date date;
        private String text;
        private List<String> likes;
        private List<Comment> comments;
        private String picture;
        private String profilePic;
        private boolean isLiked;

        public Post(String publisher, String text, String profilePic, String picture) {
            this.publisher = publisher;
            this.text = text;
            this.profilePic = profilePic;
            this.isLiked = false;
            this.date = new Date();
            this.picture = picture;
            this.likes = new ArrayList<>();
            this.comments = new ArrayList<>();
            this.pid = "default";
        }

        public List<Comment> getComments() {
            return comments;
        }

        public void setComments(List<Comment> comments) {
            this.comments = comments;
        }

        public String getLikesString() {
            return likes.size() + " " + "likes";
        }

        public boolean isLiked() {
            return isLiked;
        }

        public void setLiked(boolean liked) {
            isLiked = liked;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public List<String> getLikes() {
            return likes;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        @NonNull
        public String getPid() {
            return pid;
        }

        public String getPublisher() {
            return publisher;
        }

        public Date getDate() {
            return date;
        }

        public String getText() {
            return text;
        }

        public void setPid(@NonNull String id) {
            this.pid = id;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setLikes(List<String> likes) {
            this.likes = likes;
        }

    }

