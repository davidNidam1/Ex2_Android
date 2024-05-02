package com.example.Facybook_android.Model.interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.Facybook_android.Model.entities.Post;

import java.util.List;
@Dao
public interface PostDao {
    @Query("SELECT * FROM post")
    List<Post> index();

    @Query("SELECT * FROM post WHERE id = :id")
    Post get(int id);

    // Method to delete all entries from the database
    @Query("DELETE FROM post")
    void deleteAll();

    @Insert
    void insert(Post... posts);

    @Insert
    void insert(List<Post> posts);

    @Update
    void update(Post... posts);

    @Delete
    void delete(Post... posts);

}