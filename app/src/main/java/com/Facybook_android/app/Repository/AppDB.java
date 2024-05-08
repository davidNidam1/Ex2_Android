    package com.Facybook_android.app.Repository;

    import androidx.room.Database;
    import androidx.room.RoomDatabase;
    import androidx.room.TypeConverters;

    import com.Facybook_android.app.Model.entities.Comment;
    import com.Facybook_android.app.Model.entities.Post;
    import com.Facybook_android.app.Model.entities.User;
    import com.Facybook_android.app.Repository.interfaces.CommentDao;
    import com.Facybook_android.app.Repository.interfaces.PostDao;
    import com.Facybook_android.app.Repository.interfaces.UserDao;

    @Database(entities = {Post.class, Comment.class, User.class},  version = 27)
    @TypeConverters(Converters.class)
    public abstract class AppDB extends RoomDatabase {
        public abstract PostDao postDao();
        public abstract UserDao userDao();
        public abstract CommentDao commentDao();
    }
