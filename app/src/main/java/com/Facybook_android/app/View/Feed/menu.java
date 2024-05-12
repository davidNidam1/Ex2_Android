    package com.Facybook_android.app.View.Feed;

    import android.content.DialogInterface;
    import android.content.Intent;
    import android.content.Context;
    import android.graphics.Bitmap;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.ImageButton;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.PopupMenu;
    import androidx.lifecycle.ViewModelProvider;

    import com.Facybook_android.app.Model.entities.User;
    import com.Facybook_android.app.Model.entities.Utilities;
    import com.Facybook_android.app.R;
    import com.Facybook_android.app.View.LogIn.MainActivity;
    import com.Facybook_android.app.ViewModels.UsersViewModel;

    import java.util.Objects;

    public class menu extends AppCompatActivity {

        private ImageView user_profile;
        private TextView user_profile_name;
        private TextView view_profile_link;
        private User user;
        private UsersViewModel usersViewModel;
        private String LoggedInUser;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.menu_layout);
            usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);

            LoggedInUser = Objects.requireNonNull(Objects.requireNonNull(getIntent()
                    .getExtras()).get("LoggedInUser")).toString();
            usersViewModel.getUser(LoggedInUser);

            setViews();
            observeUserViewModel();
            setupButtons();
        }

        private void setViews() {
            user_profile = findViewById(R.id.user_profile);
            user_profile_name = findViewById(R.id.user_profile_name);
            view_profile_link = findViewById(R.id.view_profile_link);
        }

        private void setupButtons() {
            view_profile_link.setOnClickListener(v -> {
                Intent i = new Intent(menu.this, UserPage.class);
                i.putExtra("LoggedInUser", LoggedInUser);
                i.putExtra("user2view", LoggedInUser);
                startActivity(i);
            });

            TextView deleteProfileLink = findViewById(R.id.delete_profile_link);
            deleteProfileLink.setOnClickListener(view -> showDeleteConfirmationDialog());
        }

        private void showDeleteConfirmationDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete Account");
            builder.setMessage("Are you sure you want to delete this account?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked the "Yes" button, so delete the account.
                    usersViewModel.delete(user.getName());
                    Intent i = new Intent(menu.this, MainActivity.class);
                    startActivity(i);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked the "No" button, dismiss the dialog
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        public void setUserPage() {
            Bitmap bitmap = Utilities.base64ToBitmap(user.getProfilePicture());
            user_profile.setImageBitmap(bitmap);
            user_profile_name.setText(user.getName());
        }

        public void observeUserViewModel() {
            usersViewModel.get().observe(this, user -> {
                this.user = user;
                if (user != null && user.getName().equals(LoggedInUser)) {
                    setUserPage();
                    Log.e("user", "user:" + user.getName());
                }
            });
        }

        @Override
        protected void onResume() {
            super.onResume();
            usersViewModel.getUser(LoggedInUser);
        }

    }
