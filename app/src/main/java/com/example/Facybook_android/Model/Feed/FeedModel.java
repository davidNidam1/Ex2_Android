package com.example.Facybook_android.Model.Feed;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.Facybook_android.Model.adapters.CommentsListAdapter;
import com.example.Facybook_android.Model.adapters.PostsListAdapter;
import com.example.Facybook_android.Model.entities.Comment;
import com.example.Facybook_android.Model.entities.DrawableUtils;
import com.example.Facybook_android.Model.entities.Post;
import com.example.Facybook_android.Model.interfaces.PostDao;
import com.example.ex2_android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FeedModel {
    public void addExistingPosts(Context context, List<Post> posts, PostsListAdapter postsAdapter,
                                 PostDao postDao) {
//        try {
//            // Read JSON file from assets folder
//            InputStream inputStream = context.getAssets().open("posts.json");
//            int size = inputStream.available();
//            byte[] buffer = new byte[size];
//            inputStream.read(buffer);
//            inputStream.close();
//            String json = new String(buffer, StandardCharsets.UTF_8);
//
//            // Parse JSON data
//            JSONObject jsonObject = new JSONObject(json);
//            JSONArray postsArray = jsonObject.getJSONArray("posts");
//
//            for (int i = 0; i < postsArray.length(); i++) {
//                JSONObject postObject = postsArray.getJSONObject(i);
//                String username = postObject.getString("username");
//                String postContent = postObject.getString("post_content");
//                int likes = postObject.getInt("likes");
//                String time = postObject.getString("time");
//
//                // Load user profile picture from assets
//                AssetManager assetManager = context.getAssets();
//                String userProfileFileName = postObject.getString("user_profile");
//                InputStream userProfileStream = assetManager.open(userProfileFileName);
//                Drawable userProfileDrawable = Drawable.createFromStream(userProfileStream,
//                        null);
//
//                // Load post picture from assets
//                String postPictureFileName = postObject.getString("picture");
//                InputStream postPictureStream = assetManager.open(postPictureFileName);
//                Drawable postPictureDrawable = Drawable.createFromStream(postPictureStream,
//                        null);
//
//                // Add the post to the list
//                postDao.insert(new Post(username, postContent, userProfileDrawable,
//                        postPictureDrawable, likes, time));
//            }
//        } catch (IOException | JSONException e) {
//            e.printStackTrace();
//        }

        // Update the RecyclerView
        postsAdapter.setPosts(postDao.index());
    }

    public void addPost(Context context, @Nullable Intent data, PostsListAdapter postsAdapter,
                        PostDao postDao) {
            // Retrieve data from the CreateNewPost activity
            String postContent = data.getStringExtra("post_content");
            String mediaUriString = data.getStringExtra("media_uri");
            Drawable profilePic = context.getDrawable(R.drawable.user_ico);

            // Convert mediaUriString to Uri
            Uri mediaUri = null;
            if (mediaUriString != null && !mediaUriString.isEmpty()) {
                mediaUri = Uri.parse(mediaUriString);
            }
            Drawable postPic = DrawableUtils.createDrawableFromUri(context, mediaUri);

            // Create a new Post object with the retrieved data
            Post newPost = new Post("nickName", postContent, profilePic, postPic,
                    0,"right now");

            postDao.insert(newPost);
            // Add the new post to the adapter
            postsAdapter.add(newPost);
            postsAdapter.reload();
        }

    public boolean getNewPost(Context context, Uri mediaUri) {
        if (mediaUri == null) {
            Toast.makeText(context, "Please upload a picture", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void addExistingComments(Context context, String postId, List<Comment> comments,
                                    CommentsListAdapter adapter) {
        try {
            // Read JSON file from assets folder
            InputStream inputStream = context.getAssets().open("posts.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            // Parse JSON data
            JSONObject jsonObject = new JSONObject(json);
            JSONArray postsArray = jsonObject.getJSONArray("posts");

            // Iterate through each post
            for (int i = 0; i < postsArray.length(); i++) {
                JSONObject postObject = postsArray.getJSONObject(i);
                JSONArray commentsArray = postObject.getJSONArray("comments");
                String id = postObject.getString("id");
                if (id.equals(postId)) {
                    // Iterate through each comment
                    for (int j = 0; j < commentsArray.length(); j++) {
                        JSONObject commentObject = commentsArray.getJSONObject(j);
                        String username = commentObject.getString("username");
                        String commentContent = commentObject.getString("comment_content");

                        // Load user profile picture from assets
                        AssetManager assetManager = context.getAssets();
                        String userProfileFileName = commentObject.getString("user_profile");
                        InputStream userProfileStream = assetManager.open(userProfileFileName);
                        Drawable userProfileDrawable = Drawable.createFromStream(userProfileStream, null);

                        // Add the comment to the list
                        comments.add(new Comment(userProfileDrawable, commentContent, username));
                    }
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        adapter.setComments(comments);
    }

    public void addComment(Context context, CommentsListAdapter adapter, EditText comment) {
        String commentText = comment.getText().toString();
        Drawable profilePic = context.getDrawable(R.drawable.user_ico);
        Comment e = new Comment(profilePic, commentText, "nickName");
        if (!commentText.isEmpty()) {
            List<Comment> commentsL = adapter.getComments();
            commentsL.add(e);
            adapter.setComments(commentsL);
            adapter.reload();
        }
    }
}


