    package com.Facybook_android.app.View.Feed;

    import android.content.Intent;
    import android.os.Bundle;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.lifecycle.ViewModelProvider;

    import com.Facybook_android.app.Model.entities.Post;
    import com.Facybook_android.app.Model.entities.Utilities;
    import com.Facybook_android.app.R;
    import com.Facybook_android.app.View.LogIn.MainActivity;
    import com.Facybook_android.app.ViewModels.PostsViewModel;

    import java.util.List;

    public class EditPost extends AppCompatActivity {

        private EditText editTxt;
        private ImageView postPic;
        private String id;
        private String pid;
        private String text;
        private String picture;
        private PostsViewModel postsViewModel;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.edit_post_layout);
            postsViewModel = new ViewModelProvider(this).get(PostsViewModel.class);

            init();
            setViews();
            setupButtons();
        }

        private void init() {
            if(getIntent().getExtras() != null) {
                this.id = getIntent().getStringExtra("id");
                this.pid = getIntent().getStringExtra("pid");
                this.text = getIntent().getStringExtra("text");
                this.picture = getIntent().getStringExtra("picture");
            }
        }

        private void setViews() {
            editTxt = findViewById(R.id.post_content_edit);
            postPic = findViewById(R.id.post_picture);
            editTxt.setText(text);
            postPic.setImageBitmap(Utilities.base64ToBitmap(picture));
        }

        private void setupButtons() {
            Button saveBtn = findViewById(R.id.buttonSave);
            saveBtn.setOnClickListener(v -> {
                if (!editTxt.getText().toString().isEmpty()) {
                    this.text = editTxt.getText().toString();
                    postsViewModel.update(id, pid, text);
                    finish();
                }
            });
        }
    }
