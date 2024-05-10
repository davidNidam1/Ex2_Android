    package com.Facybook_android.app.View.Feed;

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

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.PopupMenu;
    import androidx.lifecycle.ViewModelProvider;

    import com.Facybook_android.app.Model.entities.User;
    import com.Facybook_android.app.Model.entities.Utilities;
    import com.Facybook_android.app.R;
    import com.Facybook_android.app.ViewModels.UsersViewModel;

    public class menu extends AppCompatActivity {

        private ImageView user_profile;
        private TextView user_profile_name;
        private TextView view_profile_link;
        private User user;
        private UsersViewModel usersViewModel;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.menu_layout);
            usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);

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
            ImageButton btnSettings = findViewById(R.id.btnSettings);
            btnSettings.setOnClickListener(v -> showPopupMenu(v));

            view_profile_link.setOnClickListener(v -> {
                Intent i = new Intent(menu.this, UserPage.class);
                startActivity(i);
            });
        }

        public void setUserPage() {
            Bitmap bitmap = Utilities.base64ToBitmap(user.getProfilePicture());
            user_profile.setImageBitmap(bitmap);
            user_profile_name.setText(user.getName());
        }

        public void observeUserViewModel() {
            usersViewModel.get().observe(this, user -> {
                this.user = user;
                if (user != null) {
                    setUserPage();
                    Log.e("user", "user:" + user.getName());
                }
            });
        }


        private void showPopupMenu(View view) {
            PopupMenu popup = new PopupMenu(this, view);
            popup.inflate(R.menu.settings_menu);
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_update_user) {
                    updateUser();
                    return true;
                } else if (item.getItemId() == R.id.action_delete_user) {
                    deleteUser();
                    return true;
                }
                return false;
            });
            popup.show();
        }

        private void updateUser() {
            // Add code to update the user
            Toast.makeText(this, "Update User Clicked", Toast.LENGTH_SHORT).show();
        }

        private void deleteUser() {
            // Add code to delete the user
            Toast.makeText(this, "Delete User Clicked", Toast.LENGTH_SHORT).show();
        }

    }
