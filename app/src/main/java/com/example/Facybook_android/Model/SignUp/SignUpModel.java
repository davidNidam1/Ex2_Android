package com.example.Facybook_android.Model.SignUp;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Facybook_android.Model.entities.Post;
//import com.example.Facybook_android.Model.entities.User;
import com.example.Facybook_android.Model.entities.Utilities;
import com.example.Facybook_android.View.Feed.Feed;
import com.example.Facybook_android.View.SignUp.SignUp;

import java.io.IOException;
import java.io.InputStream;

public class SignUpModel {
    public boolean validateSignUp(EditText usernameEditText, EditText passwordEditText,
                       EditText verifyPasswordEditText, EditText nicknameEditText,
                       Boolean pictureUploaded, Uri mediaUri, Context context) {

        // Retrieve input values
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String verifyPassword = verifyPasswordEditText.getText().toString().trim();
        String nickname = nicknameEditText.getText().toString().trim();

        // Check if any field is empty
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || !pictureUploaded ||
                TextUtils.isEmpty(verifyPassword) || TextUtils.isEmpty(nickname)) {
            Toast.makeText(context, "All fields are mandatory", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if passwords match
        if (!password.equals(verifyPassword)) {
            Toast.makeText(context, "Passwords don't match", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if password is at least 8 characters long
        if (password.length() < 8) {
            Toast.makeText(context, "Password must be at least 8 characters long",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
//        initNewUser(nickname, username, password, mediaUri, context);
        return true;

    }

//    public void initNewUser(String name, String userName, String password, Uri mediaUri,
//                            Context context) {
//        try {
//            InputStream inputStream = context.getContentResolver().openInputStream(mediaUri);
//            String profilePath = Utilities.inputStreamToBase64(inputStream);
//            User user = new User(name, profilePath, userName, password);
//            SignUp.userDao.insert(user);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
