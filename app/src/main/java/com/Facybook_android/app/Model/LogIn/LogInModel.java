    package com.Facybook_android.app.Model.LogIn;

    import static com.Facybook_android.app.Context.MyApplication.context;

    import android.content.Context;
    import android.widget.EditText;
    import android.widget.Toast;

    import com.Facybook_android.app.Model.entities.User;
    import com.Facybook_android.app.View.LogIn.MainActivity;
    import com.Facybook_android.app.View.SignUp.SignUp;

    public class LogInModel {

        public boolean logIn(User user, EditText Password) {
            String password = Password.getText().toString();

            if (user != null && password.equals(user.getPassword())) {
                return true;
            } else {
                Toast.makeText(context,
                        "Incorrect username or password", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

