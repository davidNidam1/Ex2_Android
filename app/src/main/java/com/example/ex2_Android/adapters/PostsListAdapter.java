package com.example.ex2_Android.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex2_Android.enteties.Post;
import com.example.ex2_Android.feed.comments;
import com.example.ex2_android.R;

import java.util.List;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {

    class PostViewHolder extends RecyclerView.ViewHolder {
        private final TextView author;
        private final TextView content;

        private final TextView postLikes;
        private final ImageView profilePicture;

        private final ImageView postPicture;

        private PostViewHolder(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.user_profile_name);
            content = itemView.findViewById(R.id.post_content);
            profilePicture = itemView.findViewById(R.id.user_profile_picture);
            postPicture = itemView.findViewById(R.id.post_picture);
            postLikes = itemView.findViewById(R.id.numberLikes);
        }
    }

    private final LayoutInflater mInflater;

    private List<Post> posts;

    public PostsListAdapter(Context context) { mInflater = LayoutInflater.from(context); }


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
           holder.profilePicture.setImageDrawable(current.getProfilePic());
           holder.postPicture.setImageDrawable(current.getPostPic());
           holder.postLikes.setText(current.getLikesString());


           // Find the commentBtn and set OnClickListener
           ImageButton commentButton = holder.itemView.findViewById(R.id.commentBtn);
           final int adapterPosition = holder.getAdapterPosition();
           commentButton.setOnClickListener(view -> {
               if (adapterPosition != RecyclerView.NO_POSITION && posts != null && adapterPosition < posts.size()) {
                   String postId = posts.get(adapterPosition).getId();
                   Intent intent = new Intent(view.getContext(), comments.class);
                   intent.putExtra("postId", postId);
                   view.getContext().startActivity(intent);
               }
           });

           // Find the likeBtn and set OnClickListener
           ImageButton likeButton = holder.itemView.findViewById(R.id.likeBtn);
           likeButton.setOnClickListener(view -> {
               // Toggle the like status
               current.setLiked(!current.isLiked());

               // Set the drawable resource based on the updated like status
               int drawableResource = current.isLiked() ? R.drawable.like_pressed__icon : R.drawable.like_unpressed__ico;
               likeButton.setImageResource(drawableResource);
           });
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
        notifyDataSetChanged();
    }

    public List<Post> getPosts() {
        return posts;
    }
}

