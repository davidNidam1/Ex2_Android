package com.example.Facybook_android.Model.Feed;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.format.DateUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.Facybook_android.Model.adapters.CommentsListAdapter;
import com.example.Facybook_android.Model.adapters.PostsListAdapter;
import com.example.Facybook_android.Model.entities.Comment;
import com.example.Facybook_android.Model.entities.Utilities;
import com.example.Facybook_android.Model.entities.Post;
import com.example.Facybook_android.View.Feed.Feed;
import com.example.Facybook_android.View.Feed.comments;
import com.example.ex2_android.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FeedModel {

    public void addPost(Context context, @Nullable Intent data, PostsListAdapter postsAdapter) throws FileNotFoundException {
        if (data == null) {
            return;
        }

        // Retrieve data from the CreateNewPost activity
        String postContent = data.getStringExtra("post_content");
        String mediaUriString = data.getStringExtra("media_uri");
        Drawable profilePic = context.getDrawable(R.drawable.user_ico);

        // Convert mediaUriString to Uri
        Uri mediaUri = null;
        if (mediaUriString != null && !mediaUriString.isEmpty()) {
            mediaUri = Uri.parse(mediaUriString);
        }
        try {
            assert mediaUri != null;
            InputStream inputStream = context.getContentResolver().openInputStream(mediaUri);
            String postPath = Utilities.inputStreamToBase64(inputStream);

            // Calculate the time difference between current time and post creation time
            long currentTime = System.currentTimeMillis();
            CharSequence timePassed = DateUtils.getRelativeTimeSpanString(currentTime, currentTime, DateUtils.SECOND_IN_MILLIS);

            // Create a new Post object with the retrieved data and calculated time
            Post newPost = new Post("nickName", postContent, profilePic,
                    0, timePassed.toString(), postPath);
            Feed.postDao.insert(newPost);

            // Add the new post to the adapter
            postsAdapter.add(newPost);
            postsAdapter.reload();
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

    public void addComment(Context context, CommentsListAdapter adapter, EditText comment) {
        String commentText = comment.getText().toString();
        Drawable profilePic = context.getDrawable(R.drawable.user_ico);
        Comment e = new Comment(profilePic, commentText, "nickName");
        if (!commentText.isEmpty()) {
            comments.commentDao.insert(e);
            adapter.setCommentsL(comments.commentDao.index());
            adapter.reload();
        }
    }

}


