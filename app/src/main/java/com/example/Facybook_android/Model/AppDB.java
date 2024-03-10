package com.example.Facybook_android.Model;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.Facybook_android.Model.entities.Post;
import com.example.Facybook_android.Model.interfaces.PostDao;
@Database(entities = {Post.class}, version = 2)
@TypeConverters(Converters.class)
public abstract class AppDB extends RoomDatabase {
    public abstract PostDao postDao();
}
