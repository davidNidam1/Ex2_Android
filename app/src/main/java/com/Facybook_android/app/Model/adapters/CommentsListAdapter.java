    package com.Facybook_android.app.Model.adapters;

    import android.content.Context;
    import android.graphics.Bitmap;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.EditText;
    import android.widget.ImageButton;
    import android.widget.ImageView;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.Facybook_android.app.Model.entities.Comment;
    import com.Facybook_android.app.Model.entities.Post;
    import com.Facybook_android.app.Model.entities.Utilities;
    import com.Facybook_android.app.View.Feed.comments;
    import com.Facybook_android.app.R;
    import com.Facybook_android.app.ViewModels.CommentsViewModel;
    import com.Facybook_android.app.ViewModels.PostsViewModel;

    import java.util.List;

    public class CommentsListAdapter extends RecyclerView.Adapter<CommentsListAdapter.CommentViewHolder> {

        private CommentsViewModel commentsViewModel;
        private List<Comment> comments;
        static class CommentViewHolder extends RecyclerView.ViewHolder {
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

        public CommentsListAdapter(Context context, CommentsViewModel commentsViewModel) {
            mInflater = LayoutInflater.from(context);
            this.commentsViewModel = commentsViewModel;
        }


        @NonNull
        @Override
        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = mInflater.inflate(R.layout.comment_layout, parent, false);
            return new CommentViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
            if (commentsL != null) {
                final Comment current = commentsL.get(position);
                Log.e("details", "details:" + current.getUserName() + current.getContent());
                holder.author.setText(current.getUserName());
                holder.content.setText(current.getContent());
                Bitmap bitmap = Utilities.base64ToBitmap(current.getProfilePic());
                holder.profilePicture.setImageBitmap(bitmap);

                ImageButton deleteBtn = holder.itemView.findViewById(R.id.deleteCommentBtn);
                Log.e("step0", "step0: delete btn pressed");
                deleteBtn.setOnClickListener(v -> {
                    Log.e("step1", "step1: going to delete");
                    commentsViewModel.deleteComment(current.getUserName(), current.getPostId(),
                            current.getCid());
                });

                ImageButton editBtn = holder.itemView.findViewById(R.id.editCommentBtn);
                EditText editCmt = holder.itemView.findViewById(R.id.comment_text_edit);
                TextView commentTxt = holder.itemView.findViewById(R.id.comment_text);

                editBtn.setOnClickListener(v -> {
                    allowEdit(editCmt, commentTxt, current);
                    saveEdit(editCmt, commentTxt, current, editBtn);
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
                commentsViewModel.update(comment.getUserName(), comment.getPostId(), comment);
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
