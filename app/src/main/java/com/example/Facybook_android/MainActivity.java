package com.example.Facybook_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Facybook_android.SignUp.SignUp;
import com.example.Facybook_android.feed.feed;
import com.example.ex2_android.R;

public class MainActivity extends AppCompatActivity {
    EditText Username;
    EditText Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(v -> {
            Intent i =  new Intent(this, SignUp.class);
            startActivity(i);
        });

        Username = findViewById(R.id.Username);
        Password = findViewById(R.id.Password);

        Button btnLogIn = findViewById(R.id.btnLogIn);
        btnLogIn.setOnClickListener(v -> {
            String username = Username.getText().toString();
            String password = Password.getText().toString();
            if (!username.equals("user") || !password.equals("password")) {
                Toast.makeText(MainActivity.this,
                        "Incorrect username or password", Toast.LENGTH_SHORT).show();
            } else {
                Intent i =  new Intent(this, feed.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Clear the username and password fields when the activity resumes
        Username.setText("");
        Password.setText("");
    }
}