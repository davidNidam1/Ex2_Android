package com.example.exc2_Android.enteties;
import androidx.room.PrimaryKey;
import com.example.exc2_Android.R;
@Entity
public class post {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String author;

    private String content;

    private int like;

    private int pic;



}
