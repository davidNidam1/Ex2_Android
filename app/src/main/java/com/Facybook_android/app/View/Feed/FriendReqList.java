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

import com.Facybook_android.app.Model.adapters.FriendsListAdapter;
import com.Facybook_android.app.Model.entities.User;
import com.Facybook_android.app.R;
import com.Facybook_android.app.View.LogIn.MainActivity;
import com.Facybook_android.app.ViewModels.UsersViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FriendReqList extends AppCompatActivity {
    private UsersViewModel usersViewModel;
    private FriendsListAdapter friendsListAdapter;
    private String user2view;
    private List<User> friends;
    private List<String> names;
    private TextView noReq;
    private User user;
    private String LoggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.followers_list_layout);

        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        user2view = Objects.requireNonNull(Objects.requireNonNull(getIntent()
                .getExtras()).get("user2view")).toString();
        LoggedInUser = Objects.requireNonNull(Objects.requireNonNull(getIntent()
                .getExtras()).get("LoggedInUser")).toString();
        noReq = findViewById(R.id.no_requests_yet);
        usersViewModel.reload();
        usersViewModel.getUser(user2view);

        setupRecyclerView();
        observeViewModel();
        setUpButtons();
    }

    private void setUpButtons() {
        Toolbar facybook = findViewById(R.id.toolbar);
        facybook.setOnClickListener(v -> {
            Intent i = new Intent(FriendReqList.this, Feed.class);
            i.putExtra("LoggedInUser", LoggedInUser);
            startActivity(i);
        });

        ImageButton btnMenu = findViewById(R.id.btn_menu);
        btnMenu.setOnClickListener(v -> {
            Intent i = new Intent(FriendReqList.this, menu.class);
            i.putExtra("LoggedInUser", LoggedInUser);
            startActivity(i);
        });

        TextView logoutTextView = findViewById(R.id.logoutTextView);
        logoutTextView.setOnClickListener(v -> {
            Intent i = new Intent(FriendReqList.this, MainActivity.class);
            startActivity(i);
        });
    }

    private void setupRecyclerView() {
        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        friendsListAdapter = new FriendsListAdapter(this, "requests", usersViewModel,
                user2view);
        lstPosts.setAdapter(friendsListAdapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
    }

    private void informNoReq () {
        if (names.isEmpty()) {
            noReq.setVisibility(View.VISIBLE);
        }
    }

    private void addToFriends() {
        if (!this.names.isEmpty()) {
            this.friends = new ArrayList<>();
            for (String name : this.names) {
                usersViewModel.getUser(name);
            }
        }
    }

    private void observeViewModel() {
        usersViewModel.get().observe(this, user -> {
            this.user = user;
            if (user != null && user.getName().equals(this.user2view)) {
                this.names = new ArrayList<>();
                this.names.addAll(user.getFriendRequests());
                informNoReq();
                addToFriends();
            } else if (user != null && !user.getName().equals(this.user2view)) {
                if (!friends.contains(this.user)) {
                    friends.add(this.user);
                }
                friendsListAdapter.setUsersL(friends);
            }
        });
    }
}
