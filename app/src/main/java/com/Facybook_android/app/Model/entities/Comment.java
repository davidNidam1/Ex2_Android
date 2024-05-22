    package com.Facybook_android.app.Model.entities;

    import android.graphics.drawable.Drawable;

    import androidx.annotation.NonNull;
    import androidx.room.Entity;
    import androidx.room.PrimaryKey;

    import java.util.UUID;
@Entity
    public class Comment {
    @PrimaryKey @NonNull
        private String cid;
        private String pic;
        private String content;
        private String userName;
        private String postId;

        public Comment(String pic, String content, String userName, String postId) {
            this.pic = pic;
            this.content = content;
            this.userName = userName;
            this.postId = postId;
            this.cid = "default";
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getPostId() {
            return postId;
        }

        public String getPic() {
            return pic;
        }

        public String getUserName() {
            return userName;
        }

        public String getProfilePic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getId() {
            return postId;
        }

        public void setId(String id) {
            this.postId = id;
        }
    }
