    package com.Facybook_android.app.View.Feed;

    import android.content.Intent;
    import android.os.Bundle;
    import android.text.format.DateUtils;
    import android.util.Log;
    import android.widget.ImageButton;

    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.lifecycle.ViewModelProvider;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;
    import androidx.room.Room;
    import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

    import com.Facybook_android.app.Repository.AppDB;
    import com.Facybook_android.app.Model.adapters.PostsListAdapter;
    import com.Facybook_android.app.Model.entities.Post;
    import com.Facybook_android.app.Model.Feed.FeedModel;
    import com.Facybook_android.app.Repository.interfaces.PostDao;
    import com.Facybook_android.app.R;
    import com.Facybook_android.app.ViewModels.PostsViewModel;

    import java.io.FileNotFoundException;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Timer;
    import java.util.TimerTask;

    public class Feed extends AppCompatActivity {
        private static final int REQUEST_CREATE_POST = 1001 ;
        private ShareFragment shareFragment;
        private PostsListAdapter postsAdapter;
        private SwipeRefreshLayout swipeRefreshLayout;
        private FeedModel model = new FeedModel();
        public static PostsViewModel postsViewModel;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_feed);

            postsViewModel = new ViewModelProvider(this).get(PostsViewModel.class);

            shareFragment = new ShareFragment();

            setupRecyclerView();
            setupSwipeRefresh();
            setupButtons();

            observeViewModel();
        }

        private void observeViewModel() {
            postsViewModel.get().observe(this, posts -> {
                Log.e("observer", "Observing posts list. Number of posts: " + (posts != null ? posts.size() : "null"));
                postsAdapter.setPosts(posts);
                ((SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout)).setRefreshing(false);
            });
        }

        @Override
        protected void onResume() {
            super.onResume();
            postsViewModel.startTimer();
        }

        @Override
        protected void onPause() {
            super.onPause();
            postsViewModel.stopTimer();
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
                    swipeRefreshLayout.setRefreshing(true);
                    model.addPost(this, data);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private void setupSwipeRefresh() {
            swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
            swipeRefreshLayout.setOnRefreshListener(() -> postsViewModel.reload());
        }
    }


