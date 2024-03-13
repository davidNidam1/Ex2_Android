package com.example.Facybook_android.Model;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.Facybook_android.Model.entities.Comment;
import com.example.Facybook_android.Model.entities.Post;
//import com.example.Facybook_android.Model.entities.User;
import com.example.Facybook_android.Model.interfaces.CommentDao;
import com.example.Facybook_android.Model.interfaces.PostDao;
//import com.example.Facybook_android.Model.interfaces.UserDao;

@Database(entities = {Post.class, Comment.class},  version = 15)
@TypeConverters(Converters.class)
public abstract class AppDB extends RoomDatabase {
    public abstract PostDao postDao();
//    public abstract UserDao userDao();
    public abstract CommentDao commentDao();
}
