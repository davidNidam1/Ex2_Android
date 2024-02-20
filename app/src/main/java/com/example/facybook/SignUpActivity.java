package com.example.facybook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ex2_android.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText verifyPasswordEditText;
    private EditText nicknameEditText;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Get references to all EditText fields and the sign-up button
        usernameEditText = findViewById(R.id.edit_text_username);
        passwordEditText = findViewById(R.id.edit_text_password);
        verifyPasswordEditText = findViewById(R.id.edit_text_password_verify);
        nicknameEditText = findViewById(R.id.edit_text_nickname);
        signUpButton = findViewById(R.id.button_sign_up);

        // Set a click listener for the sign-up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the text from all EditText fields
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String verifyPassword = verifyPasswordEditText.getText().toString();
                String nickname = nicknameEditText.getText().toString();

                // Perform validation
                if (!username.isEmpty() && !password.isEmpty() && password.length() >= 8 &&
                        !verifyPassword.isEmpty() && !nickname.isEmpty()) {
                    // All fields are filled, proceed with sign-up logic
                    // Clicking on Sign-up button passes the user to Log-in page.
                    Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(i);
                } else if(password.length() >= 8){
                    // Show an error message if any field is empty
                    Toast.makeText(SignUpActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    // Show an error message if the password is not at least 8 characters long
                    Toast.makeText(SignUpActivity.this, "Password must be at least 8 characters long.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}

