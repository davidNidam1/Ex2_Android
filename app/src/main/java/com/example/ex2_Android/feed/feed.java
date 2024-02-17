package com.example.ex2_Android.feed;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex2_Android.adapters.PostsListAdapter;
import com.example.ex2_Android.enteties.Post;
import com.example.ex2_android.R;

import java.util.ArrayList;
import java.util.List;

public class feed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        final PostsListAdapter adapter = new PostsListAdapter(this);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));

        List<Post> posts = new ArrayList<>();
        posts.add(new Post("Alice", "Hello World", R.drawable.user_post));
        posts.add(new Post("Alice", "Hello World", R.drawable.user_post));
        posts.add(new Post("Alice", "Hello World", R.drawable.user_post));
        posts.add(new Post("Alice", "Hello World", R.drawable.user_post));
        posts.add(new Post("Alice", "Hello World", R.drawable.user_post));
        posts.add(new Post("Alice", "Hello World", R.drawable.user_post));
        adapter.setPosts(posts);
    }
}
