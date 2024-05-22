package com.Facybook_android.app.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.Facybook_android.app.Model.entities.Comment;
import com.Facybook_android.app.Model.entities.Post;
import com.Facybook_android.app.Repository.interfaces.CommentDao;
import com.Facybook_android.app.Context.MyApplication;
import com.Facybook_android.app.R;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentAPI {

    private MutableLiveData<List<Comment>> commentListData;
    private CommentDao dao;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);

    public CommentAPI(MutableLiveData<List<Comment>> commentListData, CommentDao dao) {
        this.commentListData = commentListData;
        this.dao = dao;

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    String token = getToken();
                    if (token != null) {
                        Request.Builder builder = originalRequest.newBuilder()
                                .header("Authorization", "Bearer " + token);
                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                    }
                    return chain.proceed(originalRequest);
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    private String getToken() {
        SharedPreferences sharedPreferences = MyApplication.context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("auth_token", null);  // Default to null if the token doesn't exist
    }

    public void addComment(String userId, String postId, Comment comment) {

        Call<Comment> call = webServiceAPI.postComment(userId, postId, comment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    executor.execute(() -> {
                        try {
                            dao.insert(response.body());
                            commentListData.postValue(dao.index());
                            Log.e("Request Success!", "added comment successfully!");
                        } catch (SQLiteConstraintException e) {
                            Log.e("Database Error", "UNIQUE constraint failed: comment.cid", e);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                // Handle failure
                String errorMessage = "Failed to send request";
                Log.e("Request Failure", errorMessage, t);
            }
        });
    }

    public void delete(String userName, String postId, String cid) {
        Log.e("step4", "step4: in API delete");
        Call<Comment> call = webServiceAPI.deleteComment(userName, postId, cid);
        Log.e("step5", "step5: sent request");
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    Log.e("step6", "step6: got response");
                    executor.execute(() -> {
                        synchronized (CommentAPI.class) {
                            dao.delete(response.body());
                            commentListData.postValue(dao.index());
                            Log.e("deleteComment", "Comment deleted successfully!");
                        }
                    });
                } else {
                    handleErrorResponse(response.code());
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Log.e("Request Failure", "Failed to send request", t);
            }
        });
    }

    public void update(String userName, String postId, Comment comment) {
        Call<Comment> call = webServiceAPI.updateComment(userName, postId, comment);
        Log.e("updateC", "in update API comment");
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    executor.execute(() -> {
                        synchronized (CommentAPI.class) {
                            dao.update(response.body());
                            commentListData.postValue(dao.index());
                        }
                    });
                } else {
                    handleErrorResponse(response.code());
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Log.e("Request Failure", "Failed to send request", t);
            }
        });
    }

    private void handleErrorResponse(int code) {
        if (code == 401) {
            Toast.makeText(MyApplication.context, "Unauthorized action", Toast.LENGTH_SHORT).show();
        } else if (code == 500) {
            Toast.makeText(MyApplication.context, "Internal Server error", Toast.LENGTH_SHORT).show();
        } else if (code == 404) {
            Toast.makeText(MyApplication.context, "Comment not found", Toast.LENGTH_SHORT).show();
        }
    }

    public void getPostsComments(String userId, String postId) {
        Call<List<Comment>> call = webServiceAPI.getPostsComments(userId, postId);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful()) {
                    executor.execute(() -> {
                        synchronized (PostAPI.class) {
                           dao.deleteAll();
                           dao.insert(response.body());
                           commentListData.postValue(dao.index());
                           Log.e("PostAPI", "Fetched comments successfully");
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                // Handle the case where the API call failed to be executed
                Log.e("PostAPI", "Failed to fetch comments", t);
            }
        });
    }
    public void clear() {
        executor.execute(() -> {
            synchronized (CommentAPI.class) {
                dao.deleteAll();
                commentListData.postValue(dao.index());
            }
        });
    }
}
