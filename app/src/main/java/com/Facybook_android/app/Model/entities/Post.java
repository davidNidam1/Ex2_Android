    package com.Facybook_android.app.Model.entities;

    import android.graphics.drawable.Drawable;

    import androidx.room.Entity;
    import androidx.room.PrimaryKey;

    @Entity
    public class Post {

        @PrimaryKey(autoGenerate = true)
        private int id;
        private String publisher;
        private String date;
        private String text;
        private int likes;
    //    private List<Comment> comments;
        private String picture;
        private String profilePic;
        private boolean isLiked;
        private long creationTime;

        public Post(String publisher, String text, String profilePic, int likes,
                    String date, String picture) {
            this.publisher = publisher;
            this.text = text;
            this.profilePic = profilePic;
            this.isLiked = false;
            this.likes = likes;
            this.date = date;
            this.picture = picture;
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

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public int getLikes() {
            return likes;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

    //    public Drawable getPostPic() {
    //        return postPic;
    //    }

    //    public void setPostPic(Drawable postPic) {
    //        this.postPic = postPic;
    //    }

        public int getId() {
            return id;
        }

        public String getPublisher() {
            return publisher;
        }

        public String getDate() {
            return date;
        }

        public String getText() {
            return text;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

    }

