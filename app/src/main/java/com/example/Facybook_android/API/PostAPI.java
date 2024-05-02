package com.example.Facybook_android.API;

import androidx.lifecycle.MutableLiveData;

import com.example.Facybook_android.Model.entities.Post;
import com.example.Facybook_android.Model.interfaces.PostDao;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;1 public class PostAPI {
private MutableLiveData<List<Post>> postListData;
private PostDao dao;
Retrofit retrofit;
WebServiceAPI webServiceAPI;

        public PostAPI(MutableLiveData<List<Post>> postListData, PostDao dao) {
        this.postListData = postListData;
        this.dao = dao;

        retrofit = new Retrofit.Builder()
        .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
        .addConverterFactory(GsonConverterFactory.create())
        .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
        }

        public void get() {
        Call<List<Post>> call = webServiceAPI.getPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                     new Thread(() -> {
                        dao.clear();
                        dao.insertList(response.body());
                        postListData.postValue(dao.get());
                        }).start();
                    }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {}
        });
    }
}