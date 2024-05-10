package com.Facybook_android.app.ViewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.Facybook_android.app.Model.entities.User;
import com.Facybook_android.app.Repository.repositories.UsersRepository;

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
        mRepository.createUser(user); }
    public void getToken(User user) {
        mRepository.getToken(user);
    }

//    public void reload() { mRepository.reload(); }
    public void update(String name, String profile) { mRepository.update(name, profile); }
//    public void delete(User user) {mRepository.delete(user); }

}
