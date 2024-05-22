package com.Facybook_android.app.ViewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.Facybook_android.app.Model.entities.Comment;
import com.Facybook_android.app.Repository.repositories.CommentsRepository;

import java.util.List;

public class CommentsViewModel extends ViewModel {

    private CommentsRepository mRepository;
    private LiveData<List<Comment>> comments;

    public CommentsViewModel() {
        mRepository = new CommentsRepository();
        comments = mRepository.getAll();
    }

    public LiveData<List<Comment>> get() { return comments; }
    public void getPostsComments(String id, String pid) { mRepository.getPostsComments(id, pid);}

    public void addComment(String id, String pid, Comment e) {
        mRepository.addComment(id, pid, e);
    }

    public void deleteComment(String userName, String postId, String cid) {
        Log.e("step2", "step2: in viewModels delete");
        mRepository.deleteComment(userName, postId, cid);
    }
    public void update(String userName, String postId, Comment comment)
    { mRepository.updateComment(userName, postId, comment); }

}
