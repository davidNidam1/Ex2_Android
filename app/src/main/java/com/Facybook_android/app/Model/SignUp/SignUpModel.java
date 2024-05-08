    package com.Facybook_android.app.Model.SignUp;

    import static com.Facybook_android.app.Context.MyApplication.context;

    import android.content.Context;
    import android.net.Uri;
    import android.text.TextUtils;
    import android.util.Log;
    import android.widget.EditText;
    import android.widget.Toast;

    import com.Facybook_android.app.Model.entities.User;
    import com.Facybook_android.app.Model.entities.Utilities;
    import com.Facybook_android.app.View.Feed.Feed;
    import com.Facybook_android.app.View.LogIn.MainActivity;
    import com.Facybook_android.app.View.SignUp.SignUp;

    import java.io.IOException;
    import java.io.InputStream;

    //import com.example.Facybook_android.Model.entities.User;


    public class SignUpModel {
        public boolean validateSignUp(EditText usernameEditText, EditText passwordEditText,
                           EditText verifyPasswordEditText, EditText nicknameEditText,
                           Boolean pictureUploaded, Uri mediaUri) {

            // Retrieve input values
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String verifyPassword = verifyPasswordEditText.getText().toString().trim();
            String nickname = nicknameEditText.getText().toString().trim();

            // Check if any field is empty
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || !pictureUploaded ||
                    TextUtils.isEmpty(verifyPassword) || TextUtils.isEmpty(nickname)) {
                Toast.makeText(context, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                return false;
            }

            // Check if passwords match
            if (!password.equals(verifyPassword)) {
                Toast.makeText(context, "Passwords don't match", Toast.LENGTH_SHORT).show();
                return false;
            }

            // Check if password is at least 8 characters long
            if (password.length() < 8) {
                Toast.makeText(context, "Password must be at least 8 characters long",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            initNewUser(nickname, username, password, mediaUri, context);
            return true;

        }

        public void initNewUser(String name, String userName, String password, Uri mediaUri,
                                Context context) {
            try {
                Log.e("step1", "trying to create new user");
                InputStream inputStream = context.getContentResolver().openInputStream(mediaUri);
                String profilePicture = Utilities.inputStreamToBase64(inputStream);
                User user = new User(name, profilePicture, userName, password);
                MainActivity.usersViewModel.insert(user);

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("addUser", "Failed to add user");
            }
        }
    }
