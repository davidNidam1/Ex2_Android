package com.example.Facybook_android.Model.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Facybook_android.Model.entities.Post;
import com.example.Facybook_android.Model.interfaces.PostDao;
import com.example.Facybook_android.View.Feed.ShareFragment;
import com.example.Facybook_android.View.Feed.comments;
import com.example.ex2_android.R;

import java.util.List;
import java.util.Objects;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {

    private final ShareFragment shareFragment;
    private final PostDao postDao;

    static class PostViewHolder extends RecyclerView.ViewHolder {
        private final TextView author;
        private final TextView content;

        private final TextView postLikes;
        private final TextView timePublished;
        private final ImageView profilePicture;

        private final ImageView postPicture;

        private PostViewHolder(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.user_profile_name);
            content = itemView.findViewById(R.id.post_content);
            profilePicture = itemView.findViewById(R.id.user_profile_picture);
            postPicture = itemView.findViewById(R.id.post_picture);
            postLikes = itemView.findViewById(R.id.numberLikes);
            timePublished = itemView.findViewById(R.id.time_published);
        }
    }

    private final LayoutInflater mInflater;

    private List<Post> posts;

    public PostsListAdapter(Context context, ShareFragment shareFragment, PostDao postDao) {
        mInflater = LayoutInflater.from(context);
        this.shareFragment = shareFragment;
        this.postDao = postDao;
    }

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
           holder.timePublished.setText(current.getTimePublished());

           // Find the commentBtn and set OnClickListener
           ImageButton commentButton = holder.itemView.findViewById(R.id.commentBtn);
           final int adapterPosition = holder.getAdapterPosition();
           commentButton.setOnClickListener(view -> {
               if (adapterPosition != RecyclerView.NO_POSITION && posts != null && adapterPosition < posts.size()) {
                   int postId = posts.get(adapterPosition).getId();
                   Intent intent = new Intent(view.getContext(), comments.class);
                   intent.putExtra("postId", postId);
                   view.getContext().startActivity(intent);
               }
           });

           // Find the likeBtn and set OnClickListener
           ImageButton likeButton = holder.itemView.findViewById(R.id.likeBtn);
           TextView likesText = holder.itemView.findViewById(R.id.numberLikes);
           likeButton.setOnClickListener(view -> {
               // Toggle the like status
               current.setLiked(!current.isLiked());

               int newLike = current.isLiked() ? current.getLikes() + 1: current.getLikes() - 1;
               current.setLikes(newLike);
               String likes = current.getLikesString();

               postDao.delete(posts.get(adapterPosition));
               postDao.insert(posts.get(adapterPosition));

               // Set the drawable resource based on the updated like status
               int drawableResource = current.isLiked() ? R.drawable.like_pressed__icon : R.drawable.like_unpressed__ico;
               likeButton.setImageResource(drawableResource);
               likesText.setText(likes);

           });

           // Inside onBindViewHolder method of PostsListAdapter
           ImageButton shareButton = holder.itemView.findViewById(R.id.shareBtn);
           shareButton.setOnClickListener(v -> {
               // Get the FragmentManager from the activity
               FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();

               // Check if the fragment is already added
               if (shareFragment.isAdded()) {
                   // If the fragment is already added, check its current visibility
                   if (shareFragment.isVisible()) {
                       // If the fragment is visible, hide it
                       fragmentManager.beginTransaction().hide(shareFragment).commit();
                   } else {
                       // If the fragment is hidden, show it
                       fragmentManager.beginTransaction().show(shareFragment).commit();
                   }
               } else {
                   // If the fragment is not added, add it to the container
                   fragmentManager.beginTransaction().add(R.id.fragment_container, shareFragment).commit();
               }
           });

           ImageButton deleteBtn = holder.itemView.findViewById(R.id.deletePostBtn);
           deleteBtn.setOnClickListener(view -> {
               postDao.delete(posts.get(adapterPosition));
               remove(adapterPosition);
               reload();
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

    public void reload() {
        notifyDataSetChanged();
    }

    public void add(Post post) {
        posts.add(0, post); // Add the new post at the beginning of the list
        notifyItemInserted(0); // Notify adapter about the item insertion
    }

    public void remove(int position) {
        if (position != -1) {
            posts.remove(position); // Remove the post from the list
            notifyItemRemoved(position); // Notify adapter about the item removal
        }
    }

    public Post getPostById(String id) {
        for (Post post : posts) {
            if(Objects.equals(post.getId(), id)) {
                return post;
            }
        }
        return null;
    }
}

