    package com.Facybook_android.app.View.SignUp;

    import android.content.Intent;
    import android.net.Uri;
    import android.os.Bundle;
    import android.provider.MediaStore;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;

    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.lifecycle.ViewModelProvider;

    //import com.example.Facybook_android.Model.interfaces.UserDao;
    import com.Facybook_android.app.View.LogIn.MainActivity;
    import com.Facybook_android.app.R;
    import com.Facybook_android.app.Model.SignUp.SignUpModel;
    import com.Facybook_android.app.ViewModels.UsersViewModel;

    public class SignUp extends AppCompatActivity {

        private static final int REQUEST_IMAGE_PICK_2 = 2;
        private EditText usernameEditText, passwordEditText, verifyPasswordEditText, nicknameEditText;
        private ImageView imageViewAttachedMedia;
        private Uri mediaUri;
        private boolean pictureUploaded = false;
        private final SignUpModel model = new SignUpModel();
        private UsersViewModel usersViewModel;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sign_up);

            usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);

            // Initialize Views
            usernameEditText = findViewById(R.id.edit_text_username);
            passwordEditText = findViewById(R.id.edit_text_password);
            verifyPasswordEditText = findViewById(R.id.edit_text_password_verify);
            nicknameEditText = findViewById(R.id.edit_text_nickname);
            imageViewAttachedMedia = findViewById(R.id.user_profile_picture);

            Button uploadPictureButton = findViewById(R.id.button_upload_picture);
            Button signUpButton = findViewById(R.id.button_sign_up);

            // Set OnClickListener for upload picture button
            uploadPictureButton.setOnClickListener(v -> openGallery());

            // Set OnClickListener for sign up button
            signUpButton.setOnClickListener(v -> {
                // If all validations pass, transfer the user to the login activity
                if (model.validateSignUp(usernameEditText, passwordEditText, verifyPasswordEditText,
                        nicknameEditText, pictureUploaded, mediaUri, usersViewModel)) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish(); // close the current activity
                }
            });
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
                mediaUri = data.getData();
                imageViewAttachedMedia.setImageURI(mediaUri);
                pictureUploaded = true;
            }
        }
    }
