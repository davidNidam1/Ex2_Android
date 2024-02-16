package com.example.ex2_Android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex2_Android.enteties.Post;
import com.example.ex2_android.R;

import java.util.List;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {

    class PostViewHolder extends RecyclerView.ViewHolder {
        private final TextView author;
        private final TextView content;
        private final ImageView picture;

        private PostViewHolder(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.user_profile_name);
            content = itemView.findViewById(R.id.post_content);
            picture = itemView.findViewById(R.id.post_picture);
        }
    }

    private final LayoutInflater mInflater;

    private List<Post> posts;

    public PostsListAdapter(Context context) { mInflater = LayoutInflater.from(context); };


    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.post_layout, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
       if (posts != null) {
           final Post current = posts.get(position);
           holder.author.setText(current.getUsername());
           holder.content.setText(current.getContent());
           holder.picture.setImageResource(current.getPic());
       }
    }

    @Override
    public int getItemCount() {
        if (posts != null)
            return posts.size();
        else return 0;
    }

    public void setPosts(List<Post> s) {
        posts = s;
        notifyDataSetChanged();;
    }

    public List<Post> getPosts() {
        return posts;
    }
}

