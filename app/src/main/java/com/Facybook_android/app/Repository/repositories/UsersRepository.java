package com.Facybook_android.app.Repository.repositories;

import android.service.autofill.UserData;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.Facybook_android.app.API.UserAPI;
import com.Facybook_android.app.Context.MyApplication;
import com.Facybook_android.app.Model.entities.Token;
import com.Facybook_android.app.Model.entities.User;
import com.Facybook_android.app.Repository.AppDB;
import com.Facybook_android.app.Repository.interfaces.UserDao;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Callback;

public class UsersRepository {
    private UserDao userDao;
    private UserData userData;
    private UserAPI userAPI;

    public UsersRepository() {
        AppDB db = Room.databaseBuilder(MyApplication.context,
                AppDB.class, "UsersDB")
                .fallbackToDestructiveMigration()
                .build();
        userDao = db.userDao();
        userData = new UserData();
        userAPI = new UserAPI(userData, userDao);
    }

    class UserData extends MutableLiveData<User> {
        public UserData() {
            super();
        }

        @Override
        protected void onActive() {
            super.onActive();
            new Thread(() -> {
                userData.postValue(userDao.index());
            }).start();
        }
    }

    public LiveData<User> get() {
        return userData;
    }

    public void createUser(final User user) {
        Log.e("step3", "trying to talk to API");
        userAPI.createUser(user);
    }
    public void getUser(String id) {
        userAPI.getUser(id);
    }
    public void getToken(User user) { userAPI.fetchToken(user); }
    public void update(String name, User user) { userAPI.updateUser(name, user); }
    public void delete(String id) { userAPI.deleteUser(id); }
    public void getFriends(String name) { userAPI.getFriends(name); }
    public void sendRequest(String name) { userAPI.sendRequest(name); }
    public void reload() { userAPI.reload(); }
}