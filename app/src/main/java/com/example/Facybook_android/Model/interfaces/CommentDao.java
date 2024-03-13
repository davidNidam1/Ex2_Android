package com.example.Facybook_android.Model.interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.Facybook_android.Model.entities.Comment;
import com.example.Facybook_android.Model.entities.Post;

import java.util.List;

@Dao
public interface CommentDao {
    @Query("SELECT * FROM comment")
    List<Comment> index();

    @Query("SELECT * FROM comment WHERE id = :id")
    Comment get(int id);

    // Method to delete all entries from the database
    @Query("DELETE FROM comment")
    void deleteAll();

    @Insert
    void insert(Comment... comment);

    @Update
    void update(Comment... comment);

    @Delete
    void delete(Comment... comment);
    
}
