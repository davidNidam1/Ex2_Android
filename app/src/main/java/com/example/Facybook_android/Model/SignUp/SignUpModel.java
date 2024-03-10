package com.example.Facybook_android.Model.SignUp;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpModel {
    public boolean signUp(EditText usernameEditText, EditText passwordEditText,
                       EditText verifyPasswordEditText, EditText nicknameEditText,
                       Boolean pictureUploaded, Context context) {

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
            Toast.makeText(context, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }
}
