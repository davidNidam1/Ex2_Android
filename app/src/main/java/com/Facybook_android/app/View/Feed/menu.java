    package com.Facybook_android.app.View.Feed;

    import static com.Facybook_android.app.Context.MyApplication.context;
    import static com.Facybook_android.app.View.LogIn.MainActivity.usersViewModel;

    import android.content.Intent;
    import android.content.Context;
    import android.graphics.Bitmap;
    import android.os.Bundle;
    import android.widget.ImageView;
    import android.widget.TextView;

    import androidx.appcompat.app.AppCompatActivity;

    import com.Facybook_android.app.Model.entities.User;
    import com.Facybook_android.app.Model.entities.Utilities;
    import com.Facybook_android.app.R;
    import com.Facybook_android.app.ViewModels.UsersViewModel;

    public class menu extends AppCompatActivity {

        ImageView user_profile;
        TextView user_profile_name;
        TextView view_profile_link;
        User user;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.menu_layout);

            user_profile = findViewById(R.id.user_profile);
            user_profile_name = findViewById(R.id.user_profile_name);
            view_profile_link = findViewById(R.id.view_profile_link);
            user = usersViewModel.get().getValue();

            Bitmap bitmap = Utilities.base64ToBitmap(user.getProfilePicture());
            user_profile.setImageBitmap(bitmap);

            user_profile_name.setText(user.getName());

            view_profile_link.setOnClickListener(v -> {
                Intent i = new Intent(menu.this, UserPage.class);
                startActivity(i);
            });

        }

    }
