    package com.Facybook_android.app.View.Feed;

    import android.os.Bundle;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;

    import androidx.appcompat.app.AppCompatActivity;

    import com.Facybook_android.app.Model.entities.Post;
    import com.Facybook_android.app.Model.entities.Utilities;
    import com.Facybook_android.app.R;

    public class EditPost extends AppCompatActivity {

        private EditText editTxt;
        private ImageView postPic;
        private Post post;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.edit_post_layout);

            editTxt = findViewById(R.id.post_content_edit);
            postPic = findViewById(R.id.post_picture);

            if(getIntent().getExtras() != null) {
                int id = getIntent().getIntExtra("id", 0);
                post = Feed.postsViewModel.getPost(id);
                editTxt.setText(post.getText());
                postPic.setImageBitmap(Utilities.base64ToBitmap(post.getPicture()));
            }

            Button saveBtn = findViewById(R.id.buttonSave);
            saveBtn.setOnClickListener(v -> {
                if (post != null) {
                    post.setText(editTxt.getText().toString());
                    Feed.postsViewModel.update(post);
                    finish();
                }
            });


        }
    }
