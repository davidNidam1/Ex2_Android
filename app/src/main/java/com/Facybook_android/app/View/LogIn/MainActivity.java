    package com.Facybook_android.app.View.LogIn;

    import android.content.Intent;
    import android.os.Bundle;
    import android.widget.Button;
    import android.widget.EditText;

    import androidx.appcompat.app.AppCompatActivity;

    import com.Facybook_android.app.View.SignUp.SignUp;
    import com.Facybook_android.app.View.Feed.Feed;
    import com.Facybook_android.app.R;
    import com.Facybook_android.app.Model.LogIn.LogInModel;

    public class MainActivity extends AppCompatActivity {
        EditText Username;
        EditText Password;
        private final LogInModel model = new LogInModel();

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
                if (model.logIn(Username, Password, this)) {
                    Intent i =  new Intent(this, Feed.class);
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