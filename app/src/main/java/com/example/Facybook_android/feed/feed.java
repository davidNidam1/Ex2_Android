package com.example.Facybook_android.feed;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Facybook_android.adapters.PostsListAdapter;
import com.example.Facybook_android.enteties.DrawableUtils;
import com.example.Facybook_android.enteties.Post;
import com.example.ex2_android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class feed extends AppCompatActivity {
    private static final int REQUEST_CREATE_POST = 1001 ;
    private static final int REQUEST_EDIT_POST = 200 ;
    private ShareFragment shareFragment;
    private List<Post> posts;
    private PostsListAdapter postsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        shareFragment = new ShareFragment();
        posts = new ArrayList<>();

        setupRecyclerView();
        setupButtons();
        addExistingPosts();
    }

    private void setupRecyclerView() {
        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        postsAdapter = new PostsListAdapter(this, shareFragment);
        lstPosts.setAdapter(postsAdapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupButtons() {
        ImageButton btnMenu = findViewById(R.id.btn_menu);
        btnMenu.setOnClickListener(v -> {
            Intent i = new Intent(feed.this, menu.class);
            startActivity(i);
        });


        ImageButton btnAddPost = findViewById(R.id.btn_plus);
        btnAddPost.setOnClickListener(v -> {
            Intent i = new Intent(feed.this, CreateNewPost.class);
            startActivityForResult(i, REQUEST_CREATE_POST);
        });
    }

    private void addExistingPosts() {
        try {
            // Read JSON file from assets folder
            InputStream inputStream = getAssets().open("posts.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            // Parse JSON data
            JSONObject jsonObject = new JSONObject(json);
            JSONArray postsArray = jsonObject.getJSONArray("posts");

            for (int i = 0; i < postsArray.length(); i++) {
                JSONObject postObject = postsArray.getJSONObject(i);
                String username = postObject.getString("username");
                String postContent = postObject.getString("post_content");
                int likes = postObject.getInt("likes");
                String time = postObject.getString("time");
                String id = postObject.getString("id");

                // Load user profile picture from assets
                AssetManager assetManager = getAssets();
                String userProfileFileName = postObject.getString("user_profile");
                InputStream userProfileStream = assetManager.open(userProfileFileName);
                Drawable userProfileDrawable = Drawable.createFromStream(userProfileStream,
                        null);

                // Load post picture from assets
                String postPictureFileName = postObject.getString("picture");
                InputStream postPictureStream = assetManager.open(postPictureFileName);
                Drawable postPictureDrawable = Drawable.createFromStream(postPictureStream,
                        null);

                // Add the post to the list
                posts.add(new Post(username, postContent, userProfileDrawable, postPictureDrawable,
                        likes, id, time));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        // Update the RecyclerView
        postsAdapter.setPosts(posts);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CREATE_POST && resultCode == RESULT_OK && data != null) {
            // Retrieve data from the CreateNewPost activity
            String postContent = data.getStringExtra("post_content");
            String mediaUriString = data.getStringExtra("media_uri");
            Drawable profilePic = getDrawable(R.drawable.user_ico);

            // Convert mediaUriString to Uri
            Uri mediaUri = null;
            if (mediaUriString != null && !mediaUriString.isEmpty()) {
                mediaUri = Uri.parse(mediaUriString);
            }
            Drawable postPic = DrawableUtils.createDrawableFromUri(this, mediaUri);

            // Create a new Post object with the retrieved data
            Post newPost = new Post("nickName", postContent, profilePic, postPic,
                    0, "100", "right now");

            // Add the new post to the adapter
            postsAdapter.add(newPost);
            postsAdapter.reload();
        }
    }
}


