package com.example.Facybook_android.View.Feed;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Facybook_android.Model.Feed.FeedModel;
import com.example.Facybook_android.Model.adapters.CommentsListAdapter;
import com.example.Facybook_android.Model.entities.Comment;
import com.example.Facybook_android.Model.entities.Utilities;
import com.example.Facybook_android.Model.interfaces.CommentDao;
import com.example.myapplicationfacybook.R;


import java.util.ArrayList;
import java.util.List;

public class comments extends AppCompatActivity {
    public static CommentDao commentDao;
    private final FeedModel model = new FeedModel();
    private EditText editTxt;
    private RecyclerView lstComments;
    private CommentsListAdapter adapter;
    private List<Comment> comments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_layout);

        int postId = getIntent().getIntExtra("postId", 0);

        initialize(postId);
        setComments();

        ImageButton sendBtn = findViewById(R.id.sendButton);
        sendBtn.setOnClickListener(v -> model.addComment(this, adapter, editTxt, postId));
    }

    private void setComments() {

        if (!comments.isEmpty()) {
            adapter.setCommentsL(comments);
        }
    }

    private void initialize(int postId) {

        commentDao = Feed.db.commentDao();
        editTxt = findViewById(R.id.commentEditText);
        lstComments = findViewById(R.id.lstComments);

        adapter = new CommentsListAdapter(this);
        lstComments.setAdapter(adapter);
        lstComments.setLayoutManager(new LinearLayoutManager(this));

        comments.addAll(commentDao.getCommentsForPost(postId));
    }



}
