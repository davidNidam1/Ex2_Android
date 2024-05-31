    package com.Facybook_android.app.Model.adapters;

    import static com.Facybook_android.app.Context.MyApplication.context;

    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageButton;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.widget.PopupMenu;
    import androidx.recyclerview.widget.RecyclerView;

    import com.Facybook_android.app.Model.entities.Utilities;
    import com.Facybook_android.app.Model.entities.Post;
    import com.Facybook_android.app.View.Feed.EditPost;
    import com.Facybook_android.app.View.Feed.UserPage;
    import com.Facybook_android.app.View.Feed.comments;
    import com.Facybook_android.app.R;
    import com.Facybook_android.app.View.Feed.menu;
    import com.Facybook_android.app.View.LogIn.MainActivity;
    import com.Facybook_android.app.ViewModels.PostsViewModel;

    import java.text.SimpleDateFormat;
    import java.util.Date;
    import java.util.List;
    import java.util.TimeZone;

    public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {

        private final String LoggedInUser;
        private final String Type;
        private PostsViewModel postsViewModel;

        static class PostViewHolder extends RecyclerView.ViewHolder {
            private final TextView author;
            private final TextView content;
            private final TextView postLikes;
            private final TextView timePassed;
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
                timePassed = itemView.findViewById(R.id.time_published);
                likeButton = itemView.findViewById(R.id.likeBtn);
            }
        }

        private final LayoutInflater mInflater;

        private List<Post> posts;

        public PostsListAdapter(Context context, PostsViewModel postsViewModel, String LoggedInUser,
                                String Type) {
            mInflater = LayoutInflater.from(context);
            this.postsViewModel = postsViewModel;
            this.LoggedInUser = LoggedInUser;
            this.Type = Type;
        }

        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = mInflater.inflate(R.layout.post_layout, parent, false);
            return new PostViewHolder(itemView);
        }

        public String getTimeElapsed(Date creationDate) {
            Date now = new Date();

            // Log the current time and the creation time
            System.out.println("Current time: " + now);
            System.out.println("Creation time: " + creationDate);

            long difference = now.getTime() - creationDate.getTime(); // Difference in milliseconds

            long seconds = difference / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (days > 0) {
                return days + " days ago";
            } else if (hours > 0) {
                return hours + " hours ago";
            } else if (minutes > 0) {
                return minutes + " minutes ago";
            } else {
                return "Just now";
            }
        }


        @Override
        public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
           if (posts != null) {
               final Post current = posts.get(position);
               holder.author.setText(current.getPublisher());
               holder.content.setText(current.getText());
               holder.postLikes.setText(current.getLikesString());
               holder.timePassed.setText(getTimeElapsed(current.getDate()));

               Bitmap bitmap = Utilities.base64ToBitmap(current.getPicture());
               holder.postPicture.setImageBitmap(bitmap);
               Bitmap bitmap2 = Utilities.base64ToBitmap(current.getProfilePic());
               holder.profilePicture.setImageBitmap(bitmap2);

               // Find the commentBtn and set OnClickListener
               ImageButton commentButton = holder.itemView.findViewById(R.id.commentBtn);
               final int adapterPosition = holder.getAdapterPosition();
               commentButton.setOnClickListener(view -> {
                   if (adapterPosition != RecyclerView.NO_POSITION && posts != null && adapterPosition < posts.size()) {
                       String postId = posts.get(adapterPosition).getPid();
                       Intent intent = new Intent(view.getContext(), comments.class);
                       intent.putExtra("postId", postId);
                       intent.putExtra("LoggedInUser", LoggedInUser);
                       view.getContext().startActivity(intent);
                   }
               });

               // Find the likeBtn and set OnClickListener
               TextView likesText = holder.itemView.findViewById(R.id.numberLikes);
               List<String> likes = current.getLikes();
               // Set the drawable resource based on the updated like status
               current.setLiked(!likes.isEmpty() && likes.contains(LoggedInUser));
               int drawableResource = current.isLiked() ? R.drawable.like_pressed__icon : R.drawable.like_unpressed__ico;
               holder.likeButton.setImageResource(drawableResource);

                holder.likeButton.setOnClickListener(view -> {
                   // Toggle the like status
                   current.setLiked(!current.isLiked());

                   if (current.isLiked()) {
                       likes.add(LoggedInUser);
                       current.setLikes(likes);
                   } else {
                       likes.remove(LoggedInUser);
                       current.setLikes(likes);
                   }
                   postsViewModel.updateLikes(LoggedInUser, current.getPid(), current);

                   String likesS = current.getLikesString();
                   likesText.setText(likesS);
                   postsViewModel.reload();
               });

               // Inside onBindViewHolder method of PostsListAdapter
               ImageButton shareButton = holder.itemView.findViewById(R.id.shareBtn);
               shareButton.setOnClickListener(v -> {
                   PopupMenu popupMenu = new PopupMenu(v.getContext(), shareButton);
                   popupMenu.getMenuInflater().inflate(R.menu.share_menu, popupMenu.getMenu());

//                   popupMenu.setOnMenuItemClickListener(menuItem -> {
//                       switch (menuItem.getItemId()) {
//                           case R.id.action_messenger:
//                               Toast.makeText(v.getContext(), "Share via Messenger", Toast.LENGTH_SHORT).show();
//                               return true;
//                           case R.id.action_group:
//                               Toast.makeText(v.getContext(), "Share to Group", Toast.LENGTH_SHORT).show();
//                               return true;
//                           case R.id.action_your_story:
//                               Toast.makeText(v.getContext(), "Share to Your Story", Toast.LENGTH_SHORT).show();
//                               return true;
//                           case R.id.action_copy_link:
//                               Toast.makeText(v.getContext(), "Link Copied", Toast.LENGTH_SHORT).show();
//                               return true;
//                           default:
//                               return false;
//                       }
//                   });
                   popupMenu.show();
               });

               ImageButton deleteBtn = holder.itemView.findViewById(R.id.deletePostBtn);
               deleteBtn.setOnClickListener(view -> {
                   if(LoggedInUser.equals(current.getPublisher())) {
                       AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                       builder.setTitle("Delete Post");
                       builder.setMessage("Are you sure you want to delete this post?");

                       builder.setPositiveButton("Yes", (dialog, id) -> {
                           Post post = posts.get(adapterPosition);
                           postsViewModel.delete(post.getPublisher(), post.getPid());
                           postsViewModel.reload();
                           Toast.makeText(context,
                                   "Post deleted successfully", Toast.LENGTH_SHORT).show();
                       });
                       builder.setNegativeButton("No", (dialog, id) -> {
                           // User clicked the "No" button, dismiss the dialog
                           if (dialog != null) {
                               dialog.dismiss();
                           }
                       });

                       AlertDialog alertDialog = builder.create();
                       alertDialog.show();
                   } else {
                       Post post = posts.get(adapterPosition);
                       postsViewModel.delete(post.getPublisher(), post.getPid());
                   }
               });

               ImageButton editBtn = holder.itemView.findViewById(R.id.editPostBtn);
               editBtn.setOnClickListener(view -> {
                   if (posts.get(adapterPosition).getPublisher().equals(LoggedInUser)) {
                       Intent intent = new Intent(view.getContext(), EditPost.class);
                       intent.putExtra("id", posts.get(adapterPosition).getPublisher());
                       intent.putExtra("pid", posts.get(adapterPosition).getPid());
                       intent.putExtra("text", posts.get(adapterPosition).getText());
                       intent.putExtra("picture", posts.get(adapterPosition).getPicture());
                       view.getContext().startActivity(intent);
                       this.reload();
                   } else {
                       Toast.makeText(context,
                               "Cannot edit other users' posts", Toast.LENGTH_SHORT).show();
                   }
               });

               if (Type.equals("feed")) {
                   holder.author.setOnClickListener( v -> {
                       Intent i = new Intent(v.getContext(), UserPage.class);
                       i.putExtra("user2view", posts.get(adapterPosition).getPublisher());
                       i.putExtra("LoggedInUser", this.LoggedInUser);
                       v.getContext().startActivity(i);
                   });
               }
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

