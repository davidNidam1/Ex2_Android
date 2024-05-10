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

    public class EditPost extends AppCompatActivity {

        private EditText editTxt;
        private ImageView postPic;
        private Post post;
        private PostsViewModel postsViewModel;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.edit_post_layout);
            postsViewModel = new ViewModelProvider(this).get(PostsViewModel.class);

            setViews();
            setupButtons();
            setEdit();
        }

        private void setEdit() {
            if(getIntent().getExtras() != null) {
                int id = getIntent().getIntExtra("id", 0);
                post = postsViewModel.getPost(id);
                editTxt.setText(post.getText());
                postPic.setImageBitmap(Utilities.base64ToBitmap(post.getPicture()));
            }
        }

        private void setViews() {
            editTxt = findViewById(R.id.post_content_edit);
            postPic = findViewById(R.id.post_picture);
        }

        private void setupButtons() {
            Button saveBtn = findViewById(R.id.buttonSave);
            saveBtn.setOnClickListener(v -> {
                if (post != null) {
                    post.setText(editTxt.getText().toString());
                    postsViewModel.update(post);
                    finish();
                }
            });
        }
    }
