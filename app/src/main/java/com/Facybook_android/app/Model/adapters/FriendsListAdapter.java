package com.Facybook_android.app.Model.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Facybook_android.app.Model.entities.Post;
import com.Facybook_android.app.Model.entities.User;
import com.Facybook_android.app.Model.entities.Utilities;
import com.Facybook_android.app.R;
import com.Facybook_android.app.View.Feed.FriendReqList;
import com.Facybook_android.app.View.Feed.UserPage;
import com.Facybook_android.app.ViewModels.UsersViewModel;

import java.util.List;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendViewHolder> {

    private String type;
    private UsersViewModel usersViewModel;
    private String LoggedInUser;
    private Button acceptBtn;
    private Button denyBtn;

    class FriendViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final ImageView profilePicture;

        private FriendViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_name);
            profilePicture = itemView.findViewById(R.id.user_profile);
            acceptBtn = itemView.findViewById(R.id.btn_accept);
            denyBtn = itemView.findViewById(R.id.btn_deny);
        }
    }

    private final LayoutInflater mInflater;

    private List<User> UsersL;

    public FriendsListAdapter(Context context, String type, UsersViewModel usersViewModel,
                              String user) {
        this.type = type;
        this.LoggedInUser =user;
        this.usersViewModel = usersViewModel;
        mInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public FriendsListAdapter.FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;
        if (type.equals("friends")) {
            itemView = mInflater.inflate(R.layout.user_follows_layout, parent, false);
        } else if (type.equals("requests")) {
            itemView = mInflater.inflate(R.layout.friend_request_layout, parent, false);
        }
        return new FriendsListAdapter.FriendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsListAdapter.FriendViewHolder holder,
                                 int position) {
        if (UsersL != null) {
            final User current = UsersL.get(position);
            Log.e("friendRequestDetails", "friendRequestDetails:" + current.getName());
            Log.e("friendRequestDetails2", "friendRequestFrom:" + UsersL.get(0).getName());
            holder.name.setText(current.getName());
            Bitmap bitmap = Utilities.base64ToBitmap(current.getProfilePicture());
            holder.profilePicture.setImageBitmap(bitmap);

            if (this.type.equals("requests")) {
                acceptBtn.setOnClickListener(v -> {
                    User sender = getUsersL().get(holder.getAdapterPosition());
                    usersViewModel.acceptRequest(LoggedInUser, sender.getName());
                    denyBtn.setVisibility(View.INVISIBLE);
                    acceptBtn.setText(R.string.friends_now);
                });

                denyBtn.setOnClickListener(v -> {
                    User sender = getUsersL().get(holder.getAdapterPosition());
                    usersViewModel.denyRequest(LoggedInUser, sender.getName());
                    denyBtn.setVisibility(View.INVISIBLE);
                    acceptBtn.setText(R.string.not_friends);
                });

            }
        }
    }

    @Override
    public int getItemCount() {
        if (UsersL != null)
            return UsersL.size();
        else return 0;
    }

    public void setUsersL(List<User> c) {
        UsersL = c;
        Log.e("friendRequestDetails1", "friendRequestFrom:" + UsersL.get(0).getName());
        notifyDataSetChanged();
    }

    public List<User> getUsersL() {
        return UsersL;
    }

    public void reload() {
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if (position != -1) {
            UsersL.remove(position); // Remove the post from the list
            notifyItemRemoved(position); // Notify adapter about the item removal
        }
    }
}
