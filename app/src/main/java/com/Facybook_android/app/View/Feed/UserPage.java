    package com.Facybook_android.app.View.Feed;

    import android.content.DialogInterface;
    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.net.Uri;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ImageButton;
    import android.widget.ImageView;
    import android.widget.TextView;

    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.lifecycle.ViewModelProvider;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;
    import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

    import com.Facybook_android.app.Model.Feed.FeedModel;
    import com.Facybook_android.app.Model.adapters.PostsListAdapter;
    import com.Facybook_android.app.Model.entities.User;
    import com.Facybook_android.app.Model.entities.Utilities;
    import com.Facybook_android.app.R;
    import com.Facybook_android.app.View.LogIn.MainActivity;
    import com.Facybook_android.app.ViewModels.PostsViewModel;
    import com.Facybook_android.app.ViewModels.UsersViewModel;

    import java.io.FileNotFoundException;
    import java.util.Objects;

    public class UserPage extends AppCompatActivity {
        private static final int REQUEST_CODE_PROFILE_PICTURE = 1002;
        private static final int REQUEST_CREATE_POST = 1001 ;
        private TextView username;
        private TextView friendsCounter;
        private TextView notFriends1;
        private TextView notFriends2;
        private TextView logoutTextView;
        private ImageView user_profile_picture;
        private User user;
        private String user2view;
        private String LoggedInUser;
        private PostsListAdapter postsAdapter;
        private ShareFragment shareFragment;
        private SwipeRefreshLayout swipeRefreshLayout;
        private PostsViewModel postsViewModel;
        private UsersViewModel usersViewModel;
        private FeedModel model = new FeedModel();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.user_page_layout);
            postsViewModel = new ViewModelProvider(this).get(PostsViewModel.class);
            usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
            shareFragment = new ShareFragment();

            user2view = Objects.requireNonNull(Objects.requireNonNull(getIntent()
                    .getExtras()).get("user2view")).toString();
            LoggedInUser = Objects.requireNonNull(Objects.requireNonNull(getIntent()
                    .getExtras()).get("LoggedInUser")).toString();
            postsViewModel.clear();
            usersViewModel.reload();
            usersViewModel.getUser(user2view);

            setViews();
            setupRecyclerView();
            setupSwipeRefresh();
            observeViewModel();
            setupButtons();
        }

        private void setViews() {
            username = findViewById(R.id.username);
            friendsCounter = findViewById(R.id.friendsCounter);
            user_profile_picture = findViewById(R.id.user_profile_picture);
            logoutTextView = findViewById(R.id.logoutTextView);
            notFriends1 = findViewById(R.id.not_friends_1);
            notFriends2 = findViewById(R.id.not_friends_2);
        }

        private void setupButtons() {
            logoutTextView.setOnClickListener(v -> {
                Intent i = new Intent(UserPage.this, MainActivity.class);
                startActivity(i);
            });

            ImageView profilePic = findViewById(R.id.user_profile_picture);
            profilePic.setOnClickListener(v -> {
                Log.e("clickTest", "profileClick");
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_PROFILE_PICTURE);
            });

            ImageButton btnMenu = findViewById(R.id.btn_menu);
            btnMenu.setOnClickListener(v -> {
                Intent i = new Intent(UserPage.this, menu.class);
                i.putExtra("LoggedInUser", LoggedInUser);
                startActivity(i);
            });


            ImageButton btnAddPost = findViewById(R.id.btn_plus);
            btnAddPost.setOnClickListener(v -> {
                Intent i = new Intent(UserPage.this, CreateNewPost.class);
                startActivityForResult(i, REQUEST_CREATE_POST);
            });

            friendsCounter.setOnClickListener(v -> {
                Intent i = new Intent(UserPage.this, FriendsList.class);
                i.putExtra("user2view", user2view);
                startActivity(i);
            });

            Button friendReq = findViewById(R.id.friendRequestButton);
            friendReq.setOnClickListener(v -> {
                if (user2view.equals(LoggedInUser)) {
                    Intent i = new Intent(UserPage.this, FriendReqList.class);
                    i.putExtra("user2view", user2view);
                    startActivity(i);
                } else {
                    usersViewModel.sendRequest(LoggedInUser);
                }
            });
        }

        private void setupRecyclerView() {
            RecyclerView lstPosts = findViewById(R.id.lstPosts);
            postsAdapter = new PostsListAdapter(this, shareFragment, postsViewModel,
                    LoggedInUser, "userPage");
            lstPosts.setAdapter(postsAdapter);
            lstPosts.setLayoutManager(new LinearLayoutManager(this));
        }

        private void observeViewModel() {
            postsViewModel.get().observe(this, posts -> {
                Log.e("observer", "Observing posts list. Number of posts: " + (posts != null ? posts.size() : "null"));
                observeUserViewModel();
                assert posts != null;
                if (!posts.isEmpty()) { postsAdapter.setPosts(posts); }
                ((SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout)).setRefreshing(false);
            });
        }

        public void setUsersDetails() {
            username.setText(user2view);
            friendsCounter.setText(getString(R.string.friends_count, user.getFriends().size()));
            Bitmap bitmap = Utilities.base64ToBitmap(user.getProfilePicture());
            user_profile_picture.setImageBitmap(bitmap);

            if (!user.getFriends().contains(LoggedInUser)) {
                notFriends1.setVisibility(View.VISIBLE);
                notFriends2.setVisibility(View.VISIBLE);
            }
        }

        public void observeUserViewModel() {
            usersViewModel.get().observe(this, user -> {
                this.user = user;
                if (user != null) {
                    setUsersDetails();
                    postsViewModel.reload(user2view);
                    Log.e("user", "user:" + user.getName());
                }
            });
        }

        private void setupSwipeRefresh() {
            swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
            swipeRefreshLayout.setRefreshing(true);
            swipeRefreshLayout.setOnRefreshListener(() -> postsViewModel.reload(user2view));
        }

        @Override
        protected void onResume() {
            super.onResume();
            usersViewModel.getUser(user2view);
            if (user != null) {
                postsViewModel.reload(user2view);
            }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK && data != null) {
                if (requestCode == REQUEST_CODE_PROFILE_PICTURE) {
                    swipeRefreshLayout.setRefreshing(true);
                    Uri newPic = data.getData();
                    FeedModel.changeProfilePic(usersViewModel, user, newPic);
                } else if (requestCode == REQUEST_CREATE_POST) {
                    try {
                        swipeRefreshLayout.setRefreshing(true);
                        model.addPost(postsViewModel, user, data);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
