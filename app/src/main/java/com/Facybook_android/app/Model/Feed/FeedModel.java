    package com.Facybook_android.app.Model.Feed;

    import static com.Facybook_android.app.Context.MyApplication.context;

    import android.content.Context;
    import android.content.Intent;
    import android.graphics.drawable.Drawable;
    import android.net.Uri;
    import android.os.AsyncTask;
    import android.text.format.DateUtils;
    import android.util.Log;
    import android.widget.EditText;
    import android.widget.Toast;

    import androidx.annotation.Nullable;
    import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

    import com.Facybook_android.app.Model.adapters.CommentsListAdapter;
    import com.Facybook_android.app.Model.adapters.PostsListAdapter;
    import com.Facybook_android.app.Model.entities.Comment;
    import com.Facybook_android.app.Model.entities.User;
    import com.Facybook_android.app.Model.entities.Utilities;
    import com.Facybook_android.app.Model.entities.Post;
    import com.Facybook_android.app.View.Feed.Feed;
    import com.Facybook_android.app.View.Feed.comments;
    import com.Facybook_android.app.R;
    import com.Facybook_android.app.View.LogIn.MainActivity;
    import com.Facybook_android.app.ViewModels.CommentsViewModel;
    import com.Facybook_android.app.ViewModels.PostsViewModel;
    import com.Facybook_android.app.ViewModels.UsersViewModel;

    import java.io.FileNotFoundException;
    import java.io.IOException;
    import java.io.InputStream;
    import java.util.List;
    import java.util.UUID;

    public class FeedModel {
        public void addPost(PostsViewModel postsViewModel, User user, @Nullable Intent data) throws FileNotFoundException {
            if (data == null) {
                return;
            }

            // Retrieve data from the CreateNewPost activity
            String postContent = data.getStringExtra("post_content");
            String mediaUriString = data.getStringExtra("media_uri");
            String profilePic = user.getProfilePicture();
            String publisher = user.getName();

            // Convert mediaUriString to Uri
            Uri mediaUri = null;
            if (mediaUriString != null && !mediaUriString.isEmpty()) {
                mediaUri = Uri.parse(mediaUriString);
            }
            try {
                assert mediaUri != null;
                InputStream inputStream = context.getContentResolver().openInputStream(mediaUri);
                String postPath = Utilities.inputStreamToBase64(inputStream);

                // Create a new Post object with the retrieved data and calculated time
                Post newPost = new Post(publisher, postContent, profilePic, postPath);
                assert postContent != null;
                Log.e("postContent", postContent);
                postsViewModel.add(newPost, publisher);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public boolean getNewPost(Context context, Uri mediaUri) {
            if (mediaUri == null) {
                Toast.makeText(context, "Please upload a picture", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }

        public void addComment(String pic, EditText comment, String publisher,
                               Post post, CommentsViewModel commentsViewModel) {
            String commentText = comment.getText().toString();
            Comment e = new Comment(pic, commentText, publisher, post.getPid());
            if (!commentText.isEmpty()) {
                commentsViewModel.addComment(publisher, post.getPid(), e);
                comment.setText("");
                commentsViewModel.getPostsComments(publisher, post.getPid());
            }
        }

        public static void changeProfilePic(UsersViewModel usersViewModel, User user, Uri newPic) {
            AsyncTask<Uri, Void, User> task = new AsyncTask<Uri, Void, User>() {
                @Override
                protected User doInBackground(Uri... uris) {
                    try {
                        InputStream inputStream = context.getContentResolver().openInputStream(uris[0]);
                        user.setProfilePicture(Utilities.inputStreamToBase64(inputStream));
                        return user;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(User result) {
                    if (result != null) {
                        usersViewModel.update(user.getName(), result);
                    } else {
                        Log.e("ProfilePicUpdate", "Failed to update profile picture.");
                    }
                }
            };
            task.execute(newPic);
        }

    }


