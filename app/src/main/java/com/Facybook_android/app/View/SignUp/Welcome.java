package com.Facybook_android.app.View.SignUp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.Facybook_android.app.R;
import com.Facybook_android.app.View.Feed.Feed;
import com.Facybook_android.app.View.LogIn.MainActivity;
import com.Facybook_android.app.ViewModels.UsersViewModel;
import com.github.jinatonic.confetti.CommonConfetti;

import java.util.Objects;

public class Welcome extends AppCompatActivity {

    private FrameLayout confettiContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);

        confettiContainer = findViewById(R.id.confetti_container);
        
        setupButtons();
    }

    private void setupButtons() {
        // Start the confetti effect
        confettiContainer.post(() -> CommonConfetti.rainingConfetti(
                        confettiContainer,
                        new int[] { Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA })
                .infinite());

        Button toLogIn = findViewById(R.id.login_button);
        toLogIn.setOnClickListener( v -> {
            Intent i =  new Intent(this, MainActivity.class);
            startActivity(i);
        });
    }

}
