package com.Facybook_android.app.ViewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.Facybook_android.app.Model.entities.Post;
import com.Facybook_android.app.Model.entities.User;
import com.Facybook_android.app.Repository.repositories.UsersRepository;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class UsersViewModel extends ViewModel {
    private UsersRepository mRepository;
    private LiveData<User> userData;

    public UsersViewModel() {
        mRepository = new UsersRepository();
        userData = mRepository.get();
    }
    public LiveData<User> get() {
        return userData;
    }
    public void getUser(String id) { mRepository.getUser(id); }
    public void insert(User user) {
        Log.e("step2", "trying to talk to repository");
        mRepository.createUser(user); };

//    public void reload() { mRepository.reload(); }
//    public void update(User user) { mRepository.update(user); }
//    public void delete(User user) {mRepository.delete(user); }

}
