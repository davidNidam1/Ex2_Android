package com.example.Facybook_android.Model.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Facybook_android.Model.entities.Comment;
import com.example.Facybook_android.View.Feed.EditPost;
import com.example.Facybook_android.View.Feed.comments;
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

    private List<Comment> commentsL;

    public CommentsListAdapter(Context context) { mInflater = LayoutInflater.from(context); }


    @NonNull
    @Override
    public CommentsListAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.comment_layout, parent, false);
        return new CommentsListAdapter.CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsListAdapter.CommentViewHolder holder,
                                 int position) {
        if (commentsL != null) {
            final Comment current = commentsL.get(position);
            holder.author.setText(current.getUsername());
            holder.content.setText(current.getContent());
            holder.profilePicture.setImageDrawable(current.getProfilePic());

            ImageButton deleteBtn = holder.itemView.findViewById(R.id.deleteCommentBtn);
            final int adapterPosition = holder.getAdapterPosition();
            deleteBtn.setOnClickListener(v -> {
                comments.commentDao.delete(commentsL.get(adapterPosition));
                remove(adapterPosition);
                reload();
            });

            ImageButton editBtn = holder.itemView.findViewById(R.id.editCommentBtn);
            EditText editCmt = holder.itemView.findViewById(R.id.comment_text_edit);
            TextView commentTxt = holder.itemView.findViewById(R.id.comment_text);
            Comment comment = comments.commentDao.get(commentsL.get(adapterPosition).getId());

            editBtn.setOnClickListener(v -> {
                allowEdit(editCmt, commentTxt, comment);
                saveEdit(editCmt, commentTxt, comment, editBtn);
            });

        }
    }

    private void allowEdit(EditText editCmt, TextView commentTxt, Comment comment) {
        commentTxt.setVisibility(View.INVISIBLE);
        editCmt.setVisibility(View.VISIBLE);
        editCmt.setText(comment.getContent());
    }

    private void saveEdit(EditText editCmt, TextView commentTxt, Comment comment,
                          ImageButton editBtn) {
        editBtn.setOnClickListener(v2 -> {
            commentTxt.setVisibility(View.VISIBLE);
            editCmt.setVisibility(View.INVISIBLE);
            comment.setContent(editCmt.getText().toString());
            comments.commentDao.update(comment);
            this.setCommentsL(comments.commentDao.index());
            this.reload();
        });
    }

    @Override
    public int getItemCount() {
        if (commentsL != null)
            return commentsL.size();
        else return 0;
    }

    public void setCommentsL(List<Comment> c) {
        commentsL = c;
        notifyDataSetChanged();
    }

    public List<Comment> getCommentsL() {
        return commentsL;
    }

    public void reload() {
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if (position != -1) {
            commentsL.remove(position); // Remove the post from the list
            notifyItemRemoved(position); // Notify adapter about the item removal
        }
    }
}
