package com.Facybook_android.app.Model.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Facybook_android.app.Model.entities.Post;
import com.Facybook_android.app.Model.entities.User;
import com.Facybook_android.app.Model.entities.Utilities;
import com.Facybook_android.app.R;

import java.util.List;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendViewHolder> {

    String type;
    class FriendViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final ImageView profilePicture;

        private FriendViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_name);
            profilePicture = itemView.findViewById(R.id.user_profile);
        }
    }

    private final LayoutInflater mInflater;

    private List<User> UsersL;

    public FriendsListAdapter(Context context, String type) {
        this.type = type;
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
            holder.name.setText(current.getName());
            Bitmap bitmap = Utilities.base64ToBitmap(current.getProfilePicture());
            holder.profilePicture.setImageBitmap(bitmap);
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
