    package com.Facybook_android.app.Repository.interfaces;

    import androidx.room.Dao;
    import androidx.room.Delete;
    import androidx.room.Insert;
    import androidx.room.Query;
    import androidx.room.Update;

    import com.Facybook_android.app.Model.entities.Comment;

    import java.util.List;

    @Dao
    public interface CommentDao {
        @Query("SELECT * FROM comment")
        List<Comment> index();

        @Query("SELECT * FROM comment WHERE cid = :id")
        Comment get(String id);

        // Method to delete all entries from the database
        @Query("DELETE FROM comment")
        void deleteAll();

        // Method to get all comments for a specific post
        @Query("SELECT * FROM comment WHERE postId = :postId")
        List<Comment> getCommentsForPost(int postId);

        @Insert
        void insert(Comment... comment);

        @Insert
        void insert(List<Comment> comments);

        @Update
        void update(Comment... comment);

        @Delete
        void delete(Comment... comment);

    }
