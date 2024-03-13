package com.example.Facybook_android.View.Feed;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.Facybook_android.Model.Feed.FeedModel;
import com.example.ex2_android.R;

import java.io.ByteArrayOutputStream;

public class CreateNewPost extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 3;

    private EditText editTextPostContent;
    private ImageView imageViewAttachedMedia;
    private Button buttonAttachMedia;
    private Button buttonPost;
    private Uri mediaUri;
    private final FeedModel model = new FeedModel();

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

            if (model.getNewPost(this, mediaUri)) {
                resultIntent.putExtra("media_uri", mediaUri.toString());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void openGallery() {
        // Check if the app has permission to read external storage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it from the user
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
        } else {
            // Permission is already granted, open the dialog to choose between gallery and camera
            showMediaChooserDialog();
        }
    }

    private void showMediaChooserDialog() {
        CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Media");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                // Take Photo option selected
                openCamera();
            } else if (which == 1) {
                // Choose from Gallery option selected
                startGalleryIntent();
            } else {
                // Cancel option selected, do nothing
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            // Handle gallery image selection
            mediaUri = data.getData();
            imageViewAttachedMedia.setImageURI(mediaUri);
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            // Handle camera capture result
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // Convert Bitmap to Uri
            mediaUri = getImageUri(this, imageBitmap);
            imageViewAttachedMedia.setImageBitmap(imageBitmap);
        }
    }

    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE) {
            // Check if permission was granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open the gallery
                startGalleryIntent();
            } else {
                // Permission denied, show a message or handle accordingly
                Toast.makeText(this, "Permission denied, cannot access external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startGalleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
    }

}
