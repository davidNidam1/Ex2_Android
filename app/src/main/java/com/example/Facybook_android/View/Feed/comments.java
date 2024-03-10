package com.example.Facybook_android.View.Feed;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Facybook_android.Model.Feed.FeedModel;
import com.example.Facybook_android.Model.adapters.CommentsListAdapter;
import com.example.Facybook_android.Model.entities.Comment;
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

    private final FeedModel model = new FeedModel();

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

        model.addExistingComments(this, postId, comments, adapter);

        ImageButton sendBtn = findViewById(R.id.sendButton);
        sendBtn.setOnClickListener(v -> {
            EditText comment = findViewById(R.id.commentEditText);
            model.addComment(this, adapter, comment);
        });

    }
}
