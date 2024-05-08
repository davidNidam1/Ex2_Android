    package com.Facybook_android.app.View.Feed;

    import static com.Facybook_android.app.View.LogIn.MainActivity.usersViewModel;

    import android.graphics.Bitmap;
    import android.os.Bundle;
    import android.widget.ImageView;
    import android.widget.TextView;

    import androidx.appcompat.app.AppCompatActivity;

    import com.Facybook_android.app.Model.entities.User;
    import com.Facybook_android.app.Model.entities.Utilities;
    import com.Facybook_android.app.R;

    public class UserPage extends AppCompatActivity {
        TextView username;
        TextView friendsCounter;
        ImageView user_profile_picture;
        User user;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.user_page_layout);

            username = findViewById(R.id.username);
            friendsCounter = findViewById(R.id.friendsCounter);
            user_profile_picture = findViewById(R.id.user_profile_picture);
            user = usersViewModel.get().getValue();

            username.setText(user.getName());
            friendsCounter.setText(getString(R.string.friends_count, user.getFriends().size()));

            Bitmap bitmap = Utilities.base64ToBitmap(user.getProfilePicture());
            user_profile_picture.setImageBitmap(bitmap);


        }

    }
