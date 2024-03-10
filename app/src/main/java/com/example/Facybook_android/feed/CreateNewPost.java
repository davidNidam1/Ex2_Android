package com.example.Facybook_android.feed;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ex2_android.R;

public class CreateNewPost extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;

    private EditText editTextPostContent;
    private ImageView imageViewAttachedMedia;
    private Button buttonAttachMedia;
    private Button buttonPost;
    private Uri mediaUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_post_layout);

        editTextPostContent = findViewById(R.id.editTextPostContent);
        imageViewAttachedMedia = findViewById(R.id.imageViewAttachedMedia);
        buttonAttachMedia = findViewById(R.id.buttonAttachMedia);
        buttonPost = findViewById(R.id.buttonPost);

        buttonAttachMedia.setOnClickListener(v -> openGallery());

        buttonPost.setOnClickListener(v -> {
            String postContent = editTextPostContent.getText().toString();
            // Send back data to the feed activity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("post_content", postContent);
            if (mediaUri != null) {
                resultIntent.putExtra("media_uri", mediaUri.toString());
            }
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            // Get the Uri of the selected image
            mediaUri = data.getData();
            // Set the selected image to the imageViewAttachedMedia
            imageViewAttachedMedia.setImageURI(mediaUri);
        }
    }
}
