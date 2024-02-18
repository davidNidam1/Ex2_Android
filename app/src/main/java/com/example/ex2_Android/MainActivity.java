package com.example.ex2_Android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ex2_android.R;

import com.example.ex2_Android.feed.feed;
import com.example.ex2_Android.sign_up.sign_up;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EditText Username;
        EditText Password;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(v -> {
            Intent i =  new Intent(this, sign_up.class);
            startActivity(i);
        });

        Username = findViewById(R.id.Username);
        Password = findViewById(R.id.Password);

        Button btnLogIn = findViewById(R.id.btnLogIn);
        btnLogIn.setOnClickListener(v -> {
            String username = Username.getText().toString();
            String password = Password.getText().toString();
            if (username.equals("user") || password.equals("password")) {
                Intent i =  new Intent(this, feed.class);
                startActivity(i);
            } else {
                Toast.makeText(MainActivity.this,
                        "Incorrect username or password", Toast.LENGTH_SHORT).show();
            }

        });
    }
}