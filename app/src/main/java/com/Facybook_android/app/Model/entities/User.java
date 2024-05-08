    package com.Facybook_android.app.Model.entities;

    import androidx.annotation.NonNull;
    import androidx.room.Entity;
    import androidx.room.PrimaryKey;

    import java.util.ArrayList;
    import java.util.List;
    @Entity
    public class User {

//        @PrimaryKey
//        private int id;
        @PrimaryKey @NonNull
        private String name;
        private String profilePicture;
        private List<String> friends;
        private List<String> friendRequests;
//        private List<Post> posts;
        private String username;
        private String password;


        public User(String name, String profilePicture, String username, String password) {
            this.name = name;
            this.profilePicture = profilePicture;
            this.username = username;
            this.password = password;
            this.friendRequests = new ArrayList<>();
            this.friends = new ArrayList<>();
//            this.posts = new ArrayList<>();
        }

        public List<String> getFriendRequests() {
            return friendRequests;
        }

        public void setFriendRequests(List<String> friendRequests) {
            this.friendRequests = friendRequests;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

//        public int getId() {
//            return id;
//        }
//        public void setId(int id) {
//            this.id = id;
//        }

        public String getUserName() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfilePicture() {
            return profilePicture;
        }

        public void setProfilePicture(String profilePicture) {
            this.profilePicture = profilePicture;
        }

        public List<String> getFriends() {
            return friends;
        }

        public void setFriends(List<String> friends) {
            this.friends = friends;
        }

//        public List<Post> getPosts() {
//            return posts;
//        }
//
//        public void setPosts(List<Post> posts) {
//            this.posts = posts;
//        }

    }
