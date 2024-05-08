package com.Facybook_android.app.API;

import static com.Facybook_android.app.Context.MyApplication.context;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.Facybook_android.app.Model.entities.Post;
import com.Facybook_android.app.Model.entities.User;
import com.Facybook_android.app.R;
import com.Facybook_android.app.Repository.interfaces.UserDao;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    private MutableLiveData<User> userData;
    private UserDao dao;
    Retrofit retrofit;
    WebServiceAPI userServiceAPI;

    public UserAPI(MutableLiveData<User> userData, UserDao dao) {
        this.userData = userData;
        this.dao = dao;

        retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void createUser(User user) {
        Log.e("step4", "trying to talk to server");
        Call<Void> call = userServiceAPI.createUser(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // After adding to the server, add to the local database
                if (response.isSuccessful()) {
                    new Thread(() -> {
//                        dao.insert(user);
//                        userListData.postValue(dao.index());
                        Log.e("Request Success!", "added user successfully!");
                    }).start();
                } else if (response.code() == 409) {
                    Toast.makeText(context, "A user with this name already exists, try a new name",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
                String errorMessage = "Failed to send request";
                Log.e("Request Failure", errorMessage, t);
            }
        });
    }

    //    public void deleteUser(User user) {
//        Log.e("deleting", "trying to talk to server");
//        Call<Void> call = userServiceAPI.deleteUser(user.getId()); // Assuming you have an ID for each user
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                // After deleting from the server, remove from the local database
//                if (response.isSuccessful()) {
//                    new Thread(() -> {
//                        dao.delete(user);
//                        userListData.postValue(dao.index());
//                        Log.e("Request Success!", "deleted user successfully!");
//                    }).start();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                // Handle failure
//                String errorMessage = "Failed to send request";
//                Log.e("Request Failure", errorMessage, t);
//            }
//        });
//    }
    public void getUser(String id) {
        Call<User> call = userServiceAPI.getUser(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                new Thread(() -> {
                    dao.deleteAll();
                    dao.insert(response.body());
                    userData.postValue(dao.index());
                    Log.e("getUser", "Success!");
                }).start();
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Log the failure for debugging purposes
                Log.e("getUser", "Failed to send request", t);
            }
        });
    }
}

