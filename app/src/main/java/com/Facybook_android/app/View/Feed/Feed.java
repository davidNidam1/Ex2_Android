    package com.Facybook_android.app.View.Feed;

    import android.content.Intent;
    import android.net.Uri;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.ImageButton;
    import android.widget.ImageView;

    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.lifecycle.ViewModelProvider;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;
    import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

    import com.Facybook_android.app.Model.entities.User;
    import com.Facybook_android.app.Model.adapters.PostsListAdapter;
    import com.Facybook_android.app.Model.Feed.FeedModel;
    import com.Facybook_android.app.R;
    import com.Facybook_android.app.View.LogIn.MainActivity;
    import com.Facybook_android.app.ViewModels.PostsViewModel;
    import com.Facybook_android.app.ViewModels.UsersViewModel;

    import java.io.FileNotFoundException;
    import java.util.Objects;

    public class Feed extends AppCompatActivity {
        private static final int REQUEST_CREATE_POST = 1001 ;
        private PostsListAdapter postsAdapter;
        private SwipeRefreshLayout swipeRefreshLayout;
        private FeedModel model = new FeedModel();
        private PostsViewModel postsViewModel;
        private UsersViewModel usersViewModel;
        private User user;
        private String LoggedInUser;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_feed);
            postsViewModel = new ViewModelProvider(this).get(PostsViewModel.class);
            usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);

            LoggedInUser = Objects.requireNonNull(Objects.requireNonNull(getIntent()
                    .getExtras()).get("LoggedInUser")).toString();
            usersViewModel.getUser(LoggedInUser);

            setupRecyclerView();
            setupSwipeRefresh();
            setupButtons();
            observeViewModel();
        }

        private void observeViewModel() {
            postsViewModel.get().observe(this, posts -> {
                Log.e("observer", "Observing posts list. Number of posts: " + (posts != null ? posts.size() : "null"));
                postsAdapter.setPosts(posts);
                observeUserViewModel();
                if (user != null) {
                    postsViewModel.reload();
                }
                ((SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout)).setRefreshing(false);
            });
        }

        public void observeUserViewModel() {
            usersViewModel.get().observe(this, user -> {
                this.user = user;
                if (user != null) {
                    usersViewModel.getToken(user);
                    Log.e("user", "user:" + user.getName());
                }
            });
        }

        @Override
        protected void onResume() {
            super.onResume();
            Log.e("onResume", "active");
            usersViewModel.getUser(LoggedInUser);
            if (user != null) {
                postsViewModel.reload();
            }
        }

        private void setupRecyclerView() {
            RecyclerView lstPosts = findViewById(R.id.lstPosts);
            postsAdapter = new PostsListAdapter(this, postsViewModel,
                    LoggedInUser, "feed");
            lstPosts.setAdapter(postsAdapter);
            lstPosts.setLayoutManager(new LinearLayoutManager(this));
        }

        private void setupButtons() {
            View logoutTextView = findViewById(R.id.logoutTextView);
            logoutTextView.setOnClickListener(v -> {
                Intent i = new Intent(Feed.this, MainActivity.class);
                startActivity(i);
            });

            ImageButton btnMenu = findViewById(R.id.btn_menu);
            btnMenu.setOnClickListener(v -> {
                Intent i = new Intent(Feed.this, menu.class);
                i.putExtra("LoggedInUser", LoggedInUser);
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
            if (resultCode == RESULT_OK && data != null &&
                    requestCode == REQUEST_CREATE_POST) {
                try {
                    swipeRefreshLayout.setRefreshing(true);
                    model.addPost(postsViewModel, user, data);
                    postsViewModel.reload();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private void setupSwipeRefresh() {
            swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
            swipeRefreshLayout.setRefreshing(true);
            swipeRefreshLayout.setOnRefreshListener(() -> postsViewModel.reload());
        }
    }


