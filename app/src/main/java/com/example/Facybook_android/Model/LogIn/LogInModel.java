package com.example.Facybook_android.Model.LogIn;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

public class LogInModel {

    public boolean logIn(EditText Username, EditText Password, Context context) {
        String username = Username.getText().toString();
        String password = Password.getText().toString();

        if (!username.equals("user") || !password.equals("password")) {
            Toast.makeText(context,
                    "Incorrect username or password", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}

