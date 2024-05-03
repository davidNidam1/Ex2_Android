    package com.Facybook_android.app.View.Feed;

    import android.content.Intent;
    import android.os.Bundle;
    import android.text.format.DateUtils;
    import android.widget.ImageButton;

    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AppCompatActivity;
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

    import java.io.FileNotFoundException;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Timer;
    import java.util.TimerTask;

    public class Feed extends AppCompatActivity {
        private static final int REQUEST_CREATE_POST = 1001 ;
        private ShareFragment shareFragment;
        private List<Post> posts;
        private PostsListAdapter postsAdapter;
        private FeedModel model = new FeedModel();
        public static AppDB db;
        public static PostDao postDao;

        // Define the timer and timer task
        private Timer timer;
        private TimerTask timerTask;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_feed);

            // Initialize timer
            timer = new Timer();

            db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "PostsDB")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();

            postDao = db.postDao();

            shareFragment = new ShareFragment();
            posts = new ArrayList<>();

            setupRecyclerView();
            setupSwipeRefresh();
            setupButtons();

            if (!postDao.index().isEmpty()) {
                postsAdapter.setPosts(postDao.index());
                initializeTimerTask();
            }

        }

        @Override
        protected void onResume() {
            super.onResume();
            // Start the timer when the activity resumes
            if (!postDao.index().isEmpty()) {
                timer = new Timer();
                initializeTimerTask();
                startTimer();
            }
        }

        @Override
        protected void onPause() {
            super.onPause();

            // Stop the timer when the activity pauses
            if (!postDao.index().isEmpty()) {
                stopTimer();
            }
        }

        private void startTimer() {
            timer.schedule(timerTask, 0, 60 * 1000); // 60 seconds * 1000 milliseconds
        }

        private void stopTimer() {
            // Stop the timer
            timer.cancel();
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
                    model.addPost(this, data, postsAdapter);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private void setupSwipeRefresh() {
            SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
            swipeRefreshLayout.setOnRefreshListener(() -> {
                postsAdapter.reload();
                // Once the action is complete, call setRefreshing(false) to indicate that the refresh is complete
                swipeRefreshLayout.setRefreshing(false);
            });
        }

        private void initializeTimerTask() {
            timerTask = new TimerTask() {
                public void run() {
                    // Update the posts timePublished field
                    runOnUiThread(() -> {
                        if (postsAdapter != null) {
                            for (Post post : postsAdapter.getPosts()) {
                                if (post != null) { // Add null check here
                                    // Calculate the time difference between current time and post creation time
                                    long currentTime = System.currentTimeMillis();
                                    CharSequence timePassed = DateUtils.getRelativeTimeSpanString(post.getCreationTime(), currentTime, DateUtils.SECOND_IN_MILLIS);
                                    post.setTimePublished(timePassed.toString());
                                    // Add null check for postDao.get(post.getId()) to avoid NullPointerException
                                    Post updatedPost = postDao.get(post.getId());
                                    if (updatedPost != null) {
                                        post.setContent(updatedPost.getContent());
                                        postDao.update(post);
                                    }
                                }
                            }
                            // Notify the adapter that the data set has changed
                            postsAdapter.reload();
                        }
                    });
                }
            };
        }


    }


