package com.example.Facybook_android.View.Feed;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.Facybook_android.Model.AppDB;
import com.example.Facybook_android.Model.adapters.PostsListAdapter;
import com.example.Facybook_android.Model.entities.Post;
import com.example.Facybook_android.Model.Feed.FeedModel;
import com.example.Facybook_android.Model.interfaces.PostDao;
import com.example.ex2_android.R;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Feed extends AppCompatActivity {
    private static final int REQUEST_CREATE_POST = 1001 ;
    private static final int REQUEST_EDIT_POST = 200 ;
    private ShareFragment shareFragment;
    private List<Post> posts;
    private PostsListAdapter postsAdapter;
    private FeedModel model = new FeedModel();

    private AppDB db;
    private PostDao postDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);


        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "PostsDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        postDao = db.postDao();

        shareFragment = new ShareFragment();
        posts = new ArrayList<>();

        setupRecyclerView();
        setupButtons();
        if (!postDao.index().isEmpty()) {
            postsAdapter.setPosts(postDao.index());
        }
    }

    private void setupRecyclerView() {
        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        postsAdapter = new PostsListAdapter(this, shareFragment, postDao);
        lstPosts.setAdapter(postsAdapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupButtons() {
        ImageButton btnMenu = findViewById(R.id.btn_menu);
        btnMenu.setOnClickListener(v -> {
            Intent i = new Intent(Feed.this, menu.class);
            startActivity(i);
        });


        ImageButton btnAddPost = findViewById(R.id.btn_plus);
        btnAddPost.setOnClickListener(v -> {
            Intent i = new Intent(Feed.this, CreateNewPost.class);
            startActivityForResult(i, REQUEST_CREATE_POST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CREATE_POST && resultCode == RESULT_OK && data != null) {
            try {
                model.addPost(this, data, postsAdapter, postDao);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


