    package com.Facybook_android.app.Model.adapters;

    import static com.Facybook_android.app.Context.MyApplication.context;

    import android.content.Context;
    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.util.Log;
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

    import com.Facybook_android.app.Model.entities.Utilities;
    import com.Facybook_android.app.Model.entities.Post;
    import com.Facybook_android.app.View.Feed.EditPost;
    import com.Facybook_android.app.View.Feed.ShareFragment;
    import com.Facybook_android.app.View.Feed.comments;
    import com.Facybook_android.app.R;
    import com.Facybook_android.app.ViewModels.PostsViewModel;

    import java.util.List;

    public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {

        private final ShareFragment shareFragment;
        private PostsViewModel postsViewModel;

        static class PostViewHolder extends RecyclerView.ViewHolder {
            private final TextView author;
            private final TextView content;
            private final TextView postLikes;
            private final TextView timePublished;
            private final ImageView profilePicture;
            private final ImageView postPicture;
            private ImageButton likeButton;

            private PostViewHolder(View itemView) {
                super(itemView);
                author = itemView.findViewById(R.id.user_profile_name);
                content = itemView.findViewById(R.id.post_content);
                profilePicture = itemView.findViewById(R.id.user_profile_picture);
                postPicture = itemView.findViewById(R.id.post_picture);
                postLikes = itemView.findViewById(R.id.numberLikes);
                timePublished = itemView.findViewById(R.id.time_published);
                likeButton = itemView.findViewById(R.id.likeBtn);
            }
        }

        private final LayoutInflater mInflater;

        private List<Post> posts;

        public PostsListAdapter(Context context, ShareFragment shareFragment, PostsViewModel postsViewModel) {
            mInflater = LayoutInflater.from(context);
            this.shareFragment = shareFragment;
            this.postsViewModel = postsViewModel;
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
               holder.author.setText(current.getPublisher());
               holder.content.setText(current.getText());
               holder.postLikes.setText(current.getLikesString());
               holder.timePublished.setText(current.getDate());

               Bitmap bitmap = Utilities.base64ToBitmap(current.getPicture());
               holder.postPicture.setImageBitmap(bitmap);
               Bitmap bitmap2 = Utilities.base64ToBitmap(current.getProfilePic());
               holder.profilePicture.setImageBitmap(bitmap2);

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
               TextView likesText = holder.itemView.findViewById(R.id.numberLikes);

               // Set the drawable resource based on the updated like status
               int drawableResource = current.isLiked() ? R.drawable.like_pressed__icon : R.drawable.like_unpressed__ico;
               holder.likeButton.setImageResource(drawableResource);

                holder.likeButton.setOnClickListener(view -> {
                   // Toggle the like status
                   current.setLiked(!current.isLiked());

                   int newLike = current.isLiked() ? current.getLikes() + 1: current.getLikes() - 1;
                   current.setLikes(newLike);
                   String likes = current.getLikesString();
                   likesText.setText(likes);

                   postsViewModel.update(posts.get(adapterPosition));
                   postsViewModel.reload();
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
                   Log.e("delete", "deletebtn pressed");
                   Post post = posts.get(adapterPosition);
                   postsViewModel.delete(post.getPublisher(), post.getId());
//                   postsViewModel.reload();
               });

               ImageButton editBtn = holder.itemView.findViewById(R.id.editPostBtn);
               editBtn.setOnClickListener(view -> {
                   Intent intent = new Intent(view.getContext(), EditPost.class);
                   intent.putExtra("id", posts.get(adapterPosition).getId());
                   view.getContext().startActivity(intent);
                   this.reload();
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

        public void reload() {
            notifyDataSetChanged();
        }

    }

