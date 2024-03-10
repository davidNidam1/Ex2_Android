package com.example.Facybook_android.SignUp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Facybook_android.MainActivity;
import com.example.ex2_android.R;

public class SignUp extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK_2 = 2;
    private EditText usernameEditText, passwordEditText, verifyPasswordEditText, nicknameEditText;
    private Button uploadPictureButton, signUpButton;

    private ImageView imageViewAttachedMedia;

    private boolean pictureUploaded = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Views
        usernameEditText = findViewById(R.id.edit_text_username);
        passwordEditText = findViewById(R.id.edit_text_password);
        verifyPasswordEditText = findViewById(R.id.edit_text_password_verify);
        nicknameEditText = findViewById(R.id.edit_text_nickname);
        uploadPictureButton = findViewById(R.id.button_upload_picture);
        signUpButton = findViewById(R.id.button_sign_up);

        // Set OnClickListener for upload picture button
        uploadPictureButton.setOnClickListener(v -> openGallery());

        // Set OnClickListener for sign up button
        signUpButton.setOnClickListener(v -> signUp());
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK_2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK_2 && resultCode == RESULT_OK && data != null) {
            // Get the Uri of the selected image
            Uri mediaUri = data.getData();
            pictureUploaded = true;

        }
    }

    private void signUp() {
        // Retrieve input values
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String verifyPassword = verifyPasswordEditText.getText().toString().trim();
        String nickname = nicknameEditText.getText().toString().trim();

        // Check if any field is empty
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || !pictureUploaded ||
                TextUtils.isEmpty(verifyPassword) || TextUtils.isEmpty(nickname)) {
            Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if passwords match
        if (!password.equals(verifyPassword)) {
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if password is at least 8 characters long
        if (password.length() < 8) {
            Toast.makeText(this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        // If all validations pass, transfer the user to the login activity
        Intent intent = new Intent(SignUp.this, MainActivity.class);
        startActivity(intent);
        finish(); // close the current activity
    }
}
