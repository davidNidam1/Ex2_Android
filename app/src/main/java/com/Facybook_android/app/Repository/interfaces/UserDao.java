    package com.Facybook_android.app.Repository.interfaces;

    import androidx.room.Dao;
    import androidx.room.Delete;
    import androidx.room.Insert;
    import androidx.room.Query;

    import com.Facybook_android.app.Model.entities.Post;
    import com.Facybook_android.app.Model.entities.User;

    import java.util.List;

    @Dao
    public interface UserDao {
//        @Query("SELECT * FROM user WHERE name = :name")
//        User get(String name);
        @Query("SELECT * FROM user")
        User index();
//        @Query("DELETE FROM user")
//        void deleteAll();
        @Insert
        void insert(User... users);
        @Delete
        void delete(User... users);
        @Query("DELETE FROM user")
        void deleteAll();
    }
