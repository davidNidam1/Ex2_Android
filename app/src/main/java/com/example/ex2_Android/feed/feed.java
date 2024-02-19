package com.example.ex2_Android.feed;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex2_Android.adapters.PostsListAdapter;
import com.example.ex2_Android.enteties.Post;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        final PostsListAdapter postsAdapter = new PostsListAdapter(this);
        lstPosts.setAdapter(postsAdapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));

        List<Post> posts = new ArrayList<>();
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
                String id = postObject.getString("id");

                 //Load user profile picture from assets
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
                        likes, id));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        postsAdapter.setPosts(posts);

        ImageButton btnMenu = findViewById(R.id.btn_menu);
        btnMenu.setOnClickListener(v -> {
            Intent i =  new Intent(feed.this, menu.class);
                startActivity(i);
        });

    }

}
