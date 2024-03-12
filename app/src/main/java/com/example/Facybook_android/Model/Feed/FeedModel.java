package com.example.Facybook_android.Model.Feed;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.Facybook_android.Model.adapters.CommentsListAdapter;
import com.example.Facybook_android.Model.adapters.PostsListAdapter;
import com.example.Facybook_android.Model.entities.Comment;
import com.example.Facybook_android.Model.entities.Utilities;
import com.example.Facybook_android.Model.entities.Post;
import com.example.Facybook_android.Model.interfaces.PostDao;
import com.example.ex2_android.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FeedModel {

    public void addPost(Context context, @Nullable Intent data, PostsListAdapter postsAdapter,
                        PostDao postDao) throws FileNotFoundException {
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
            InputStream inputStream = context.getContentResolver().openInputStream(mediaUri);
            String postPath = Utilities.inputStreamToBase64(inputStream);
            // Create a new Post object with the retrieved data
            Post newPost = new Post("nickName", postContent, profilePic,
                    0, "right now", postPath);
            postDao.insert(newPost);
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
            List<Comment> commentsL = adapter.getComments();
            commentsL.add(e);

            adapter.setComments(commentsL);
            adapter.reload();
        }
    }
}


