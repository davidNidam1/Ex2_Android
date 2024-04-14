//package com.example.Facybook_android.Model.interfaces;
//
//import androidx.room.Dao;
//import androidx.room.Delete;
//import androidx.room.Insert;
//import androidx.room.Query;
//import androidx.room.Update;
//
//import com.example.Facybook_android.Model.entities.User;
//
//import java.util.List;
//
//@Dao
//public interface UserDao {
//    @Query("SELECT COUNT(*) FROM user WHERE userName = :username AND password = :password")
//    int findUserByUsernameAndPassword(String username, String password);
//    @Query("SELECT * FROM user")
//    List<User> index();
//
//    @Query("SELECT * FROM user WHERE id = :id")
//    User get(int id);
//
//    // Method to delete all entries from the database
//    @Query("DELETE FROM user")
//    void deleteAll();
//
//    @Insert
//    void insert(User... users);
//
//    @Update
//    void update(User... users);
//
//    @Delete
//    void delete(User... users);
//
//}
