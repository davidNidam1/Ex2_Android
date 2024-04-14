package com.example.Facybook_android.View.Feed;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.Facybook_android.Model.AppDB;
import com.example.Facybook_android.Model.entities.Post;
import com.example.Facybook_android.Model.entities.Utilities;
import com.example.Facybook_android.Model.interfaces.PostDao;
import com.example.myapplicationfacybook.R;


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
            post = Feed.postDao.get(id);
            editTxt.setText(post.getContent());
            postPic.setImageBitmap(Utilities.base64ToBitmap(post.getPostPath()));
        }

        Button saveBtn = findViewById(R.id.buttonSave);
        saveBtn.setOnClickListener(v -> {
            if (post != null) {
                post.setContent(editTxt.getText().toString());
                Feed.postDao.update(post);
                finish();
            }
        });


    }
}
