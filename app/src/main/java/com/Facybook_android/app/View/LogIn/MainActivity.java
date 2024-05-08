    package com.Facybook_android.app.View.LogIn;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.widget.Button;
    import android.widget.EditText;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.lifecycle.ViewModel;
    import androidx.lifecycle.ViewModelProvider;
    import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

    import com.Facybook_android.app.Model.entities.User;
    import com.Facybook_android.app.View.SignUp.SignUp;
    import com.Facybook_android.app.View.Feed.Feed;
    import com.Facybook_android.app.R;
    import com.Facybook_android.app.Model.LogIn.LogInModel;
    import com.Facybook_android.app.ViewModels.PostsViewModel;
    import com.Facybook_android.app.ViewModels.UsersViewModel;

    public class MainActivity extends AppCompatActivity {
        EditText Nickname;
        EditText Password;
        private final LogInModel model = new LogInModel();
        private User user;
        public static UsersViewModel usersViewModel;

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
            observeViewModel();

            Button btnSignUp = findViewById(R.id.btnSignUp);
            btnSignUp.setOnClickListener(v -> {
                Intent i =  new Intent(this, SignUp.class);
                startActivity(i);
            });

            Nickname = findViewById(R.id.edit_text_nickname);
            Password = findViewById(R.id.Password);

            Button btnLogIn = findViewById(R.id.btnLogIn);
            btnLogIn.setOnClickListener(v -> {
                usersViewModel.getUser(Nickname.getText().toString());
                if (model.logIn(user, Password)) {
                    Intent i =  new Intent(this, Feed.class);
                    startActivity(i);
                }
            });
        }

        private void observeViewModel() {
            usersViewModel.get().observe(this, user -> {
                this.user = user;
            });
        }

        @Override
        protected void onResume() {
            super.onResume();
            // Clear the username and password fields when the activity resumes
            Nickname.setText("");
            Password.setText("");
        }
    }