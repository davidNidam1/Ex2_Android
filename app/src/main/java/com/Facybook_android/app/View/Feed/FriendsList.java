package com.Facybook_android.app.View.Feed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.Facybook_android.app.Model.adapters.FriendsListAdapter;
import com.Facybook_android.app.Model.entities.User;
import com.Facybook_android.app.R;
import com.Facybook_android.app.View.LogIn.MainActivity;
import com.Facybook_android.app.ViewModels.UsersViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FriendsList extends AppCompatActivity {

    private UsersViewModel usersViewModel;
    private FriendsListAdapter friendsListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String user2view;
    private List<User> friends;
    private List<String> names;
    private User user;
    private String LoggedInUser;
    private TextView noFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.followers_list_layout);

        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        user2view = Objects.requireNonNull(Objects.requireNonNull(getIntent()
                .getExtras()).get("user2view")).toString();
        LoggedInUser = Objects.requireNonNull(Objects.requireNonNull(getIntent()
                .getExtras()).get("LoggedInUser")).toString();
        noFriends = findViewById(R.id.no_friends_yet);
        usersViewModel.getFriends(user2view);

        setupRecyclerView();
        setupSwipeRefresh();
        observeViewModel();
        setUpButtons();
    }

    private void setUpButtons() {
        Toolbar facybook = findViewById(R.id.toolbar);
        facybook.setOnClickListener(v -> {
            Intent i = new Intent(FriendsList.this, Feed.class);
            i.putExtra("LoggedInUser", LoggedInUser);
            startActivity(i);
        });

        ImageButton btnMenu = findViewById(R.id.btn_menu);
        btnMenu.setOnClickListener(v -> {
            Intent i = new Intent(FriendsList.this, menu.class);
            i.putExtra("LoggedInUser", LoggedInUser);
            startActivity(i);
        });

        TextView logoutTextView = findViewById(R.id.logoutTextView);
        logoutTextView.setOnClickListener(v -> {
            Intent i = new Intent(FriendsList.this, MainActivity.class);
            startActivity(i);
        });
    }

    private void setupRecyclerView() {
        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        friendsListAdapter = new FriendsListAdapter(this, "friends", usersViewModel,
                user2view);
        lstPosts.setAdapter(friendsListAdapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(() ->  {
            usersViewModel.getFriends(user2view);
            if (!friends.isEmpty()) {
                friendsListAdapter.setUsersL(friends);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void addToFriends() {
        if (!this.names.isEmpty()) {
            this.friends = new ArrayList<>();
            for (String name : this.names) {
                Log.e("friendsL", "friend1:" + name);
                usersViewModel.getUser(name);
            }
        }
    }

    private void informNoFriends () {
        if (names.size() - 1 == 0) {
            noFriends.setVisibility(View.VISIBLE);
        }
    }

    private void observeViewModel() {
        usersViewModel.get().observe(this, user -> {
            this.user = user;
            if (user != null && user.getName().equals(this.user2view)) {
                this.names = new ArrayList<>();
                this.names.addAll(user.getFriends());
                informNoFriends();
                addToFriends();
            } else if (user != null && !user.getName().equals(this.user2view)) {
                if (!friends.contains(this.user)) {
                    Log.e("friendsL", "friend2:" + this.user.getName());
                    friends.add(this.user);
                }
                friendsListAdapter.setUsersL(friends);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
