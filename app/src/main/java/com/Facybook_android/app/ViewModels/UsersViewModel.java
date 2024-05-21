package com.Facybook_android.app.ViewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.Facybook_android.app.Model.entities.User;
import com.Facybook_android.app.Repository.repositories.UsersRepository;

import java.util.List;

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
    public void getToken(User user) { mRepository.getToken(user); }
    public void update(String name, User user) { mRepository.update(name, user); }
    public void delete(String id) { mRepository.delete(id); }
    public void getFriends(String name) { mRepository.getFriends(name); }
    public void sendRequest(String name) { mRepository.sendRequest(name); }
    public void reload() { mRepository.reload(); }
    public void acceptRequest(String loggedInUser, String sender) {
        mRepository.acceptRequest(loggedInUser, sender); }

    public void denyRequest(String loggedInUser, String sender) {
        mRepository.denyRequest(loggedInUser, sender); }
}
