package com.example.Facybook_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Facybook_android.enteties.Comment;
import com.example.ex2_android.R;

import java.util.List;

public class CommentsListAdapter extends RecyclerView.Adapter<CommentsListAdapter.CommentViewHolder> {

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private final TextView author;
        private final TextView content;
        private final ImageView profilePicture;

        private CommentViewHolder(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.user_name);
            content = itemView.findViewById(R.id.comment_text);
            profilePicture = itemView.findViewById(R.id.user_profile);
        }
    }

    private final LayoutInflater mInflater;

    private List<Comment> comments;

    public CommentsListAdapter(Context context) { mInflater = LayoutInflater.from(context); }


    @NonNull
    @Override
    public CommentsListAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.comment_layout, parent, false);
        return new CommentsListAdapter.CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsListAdapter.CommentViewHolder holder, int position) {
        if (comments != null) {
            final Comment current = comments.get(position);
            holder.author.setText(current.getUsername());
            holder.content.setText(current.getContent());
            holder.profilePicture.setImageDrawable(current.getProfilePic());

            ImageButton deleteBtn = holder.itemView.findViewById(R.id.deleteCommentBtn);
            final int adapterPosition = holder.getAdapterPosition();
            deleteBtn.setOnClickListener(v -> {
                remove(adapterPosition);
                reload();
            });

        }
    }

    @Override
    public int getItemCount() {
        if (comments != null)
            return comments.size();
        else return 0;
    }

    public void setComments(List<Comment> c) {
        comments = c;
        notifyDataSetChanged();
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void reload() {
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if (position != -1) {
            comments.remove(position); // Remove the post from the list
            notifyItemRemoved(position); // Notify adapter about the item removal
        }
    }
}
