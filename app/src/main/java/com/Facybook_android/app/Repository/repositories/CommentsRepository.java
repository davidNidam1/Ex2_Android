package com.Facybook_android.app.Repository.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.Facybook_android.app.API.CommentAPI;
import com.Facybook_android.app.API.PostAPI;
import com.Facybook_android.app.Context.MyApplication;
import com.Facybook_android.app.Model.entities.Comment;
import com.Facybook_android.app.Model.entities.Post;
import com.Facybook_android.app.Repository.AppDB;
import com.Facybook_android.app.Repository.interfaces.CommentDao;
import com.Facybook_android.app.Repository.interfaces.PostDao;

import java.util.LinkedList;
import java.util.List;

public class CommentsRepository {
    private CommentDao dao;
    private CommentsRepository.CommentListData commentListData;
    private CommentAPI api;

    public CommentsRepository() {
        AppDB db = Room.databaseBuilder(MyApplication.context,
                        AppDB.class, "commentsDB")
                .fallbackToDestructiveMigration()
                .build();
        dao = db.commentDao();
        commentListData = new CommentsRepository.CommentListData();
        api = new CommentAPI(commentListData, dao);
    }
    class CommentListData extends MutableLiveData<List<Comment>> {
        public CommentListData() {
            super();
            setValue(new LinkedList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();
            new Thread(() -> commentListData.postValue(dao.index())).start();
        }

    }

    public LiveData<List<Comment>> getAll() {
        return commentListData;
    }

    public void getPostsComments(String id, String pid) { api.getPostsComments(id, pid);}
    public void addComment(String id, String pid, Comment e) {
        api.addComment(id, pid, e); }
    public void deleteComment(String userName, String postId, String cid) {
        Log.e("step3", "step5: in repositories delete");
        api.delete(userName, postId, cid);
    }
    public void updateComment(String userName, String postId, Comment comment) {
        api.update(userName, postId, comment);
    }
}
