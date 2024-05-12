package com.Facybook_android.app.View.Feed;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Facybook_android.app.Model.adapters.FriendsListAdapter;
import com.Facybook_android.app.Model.entities.User;
import com.Facybook_android.app.R;
import com.Facybook_android.app.ViewModels.UsersViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FriendReqList extends AppCompatActivity {
    private UsersViewModel usersViewModel;
    private FriendsListAdapter friendsListAdapter;
    private String user2view;
    private List<User> friends;
    private TextView noReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.followers_list_layout);

        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        user2view = Objects.requireNonNull(Objects.requireNonNull(getIntent()
                .getExtras()).get("user2view")).toString();
        noReq = findViewById(R.id.no_requests_yet);

        setupRecyclerView();
        observeViewModel();
    }

    private void setupRecyclerView() {
        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        friendsListAdapter = new FriendsListAdapter(this, "requests");
        lstPosts.setAdapter(friendsListAdapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
    }

    private void informNoReq () {
        if (friends.isEmpty()) {
            noReq.setVisibility(View.VISIBLE);
        } else {
            noReq.setVisibility(View.INVISIBLE);
        }
    }

    private void observeViewModel() {
        usersViewModel.get().observe(this, user -> {
            friends = new ArrayList<>();
            if (user != null && Objects.equals(user.getName(), this.user2view)) {
                for ( String name : user.getFriendRequests()) {
                    usersViewModel.getUser(name);
                    User friend = usersViewModel.get().getValue();
                    friends.add(friend);
                }
                friendsListAdapter.setUsersL(friends);
            }
            informNoReq();
        });
    }
}
