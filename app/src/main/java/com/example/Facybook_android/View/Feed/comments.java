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
import com.example.ex2_android.R;

import java.util.ArrayList;
import java.util.List;

public class comments extends AppCompatActivity {
    public static CommentDao commentDao;
    private final FeedModel model = new FeedModel();
    private EditText editTxt;
    private  Comment comment;
    private RecyclerView lstComments;
    private CommentsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_layout);

        initialize();
        setComments();

        ImageButton sendBtn = findViewById(R.id.sendButton);
        sendBtn.setOnClickListener(v -> model.addComment(this, adapter, editTxt));


//        String postId = getIntent().getStringExtra("postId");
//        List<Comment> comments = new ArrayList<>();
    }

    private void setComments() {

        if (!commentDao.index().isEmpty()) {
            adapter.setCommentsL(commentDao.index());
        }
    }

    private void initialize() {

        commentDao = Feed.db.commentDao();
        editTxt = findViewById(R.id.commentEditText);
        lstComments = findViewById(R.id.lstComments);

        adapter = new CommentsListAdapter(this);
        lstComments.setAdapter(adapter);
        lstComments.setLayoutManager(new LinearLayoutManager(this));

    }



}
