package com.example.ex2_Android.feed;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex2_Android.adapters.CommentsListAdapter;
import com.example.ex2_Android.enteties.Comment;
import com.example.ex2_android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class comments extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_layout);

        RecyclerView lstComments = findViewById(R.id.lstComments);
        final CommentsListAdapter adapter = new CommentsListAdapter(this);
        lstComments.setAdapter(adapter);
        lstComments.setLayoutManager(new LinearLayoutManager(this));

        String postId = getIntent().getStringExtra("postId");
        List<Comment> comments = new ArrayList<>();
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
                        AssetManager assetManager = getAssets();
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

        ImageButton sendBtn = findViewById(R.id.sendButton);
        sendBtn.setOnClickListener(v -> {
            EditText comment = findViewById(R.id.commentEditText);
            String commentText = comment.getText().toString();
            Drawable profilePic = getDrawable(R.drawable.user_ico);
            Comment e = new Comment(profilePic, commentText, "nickName");
            if (!commentText.isEmpty()) {
                List<Comment> commentsL = adapter.getComments();
                commentsL.add(e);
                adapter.setComments(commentsL);
                adapter.reload();
            }
        });

    }
}
