    package com.Facybook_android.app.API;

    import static com.Facybook_android.app.Context.MyApplication.context;

    import android.content.Context;
    import android.content.SharedPreferences;
    import android.database.sqlite.SQLiteConstraintException;
    import android.util.Log;
    import android.widget.Toast;

    import androidx.lifecycle.MutableLiveData;

    import com.Facybook_android.app.Model.entities.Comment;
    import com.Facybook_android.app.Model.entities.Post;
    import com.Facybook_android.app.Model.entities.User;
    import com.Facybook_android.app.Repository.interfaces.PostDao;
    import com.Facybook_android.app.Context.MyApplication;
    import com.Facybook_android.app.R;

    import org.json.JSONObject;

    import java.util.List;
    import java.util.concurrent.ExecutorService;
    import java.util.concurrent.Executors;

    import okhttp3.MediaType;
    import okhttp3.OkHttpClient;
    import okhttp3.Request;
    import okhttp3.RequestBody;
    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;
    import retrofit2.Retrofit;
    import retrofit2.converter.gson.GsonConverterFactory;
    public class PostAPI {
    private MutableLiveData<List<Post>> postListData;
    private PostDao dao;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);

        public PostAPI(MutableLiveData<List<Post>> postListData, PostDao dao) {
            this.postListData = postListData;
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
            .baseUrl(context.getString(R.string.BaseUrl))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
            webServiceAPI = retrofit.create(WebServiceAPI.class);
            }

        private String getToken() {
            SharedPreferences sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
            return sharedPreferences.getString("auth_token", null);  // Default to null if the token doesn't exist
        }

            public void get() {
            Call<List<Post>> call = webServiceAPI.getPosts();
            call.enqueue(new Callback<List<Post>>() {
                @Override
                public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                    executor.execute(() -> {
                        synchronized (PostAPI.class) {
                            dao.deleteAll();
                            dao.insert(response.body());
                            postListData.postValue(response.body());
                            Log.e("UserRepository", "Fetched posts successfully");
                        }
                    });}
                @Override
                public void onFailure(Call<List<Post>> call, Throwable t) {}
            });
        }

        public void add(Post post, String id) {
            Call<Post> call = webServiceAPI.createPost(id, post);
            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    // After adding to the server, add to the local database
                    if (response.isSuccessful()) {
                        executor.execute(() -> {
                            synchronized (PostAPI.class) {
                                try {
                                    dao.insert(response.body());
                                    postListData.postValue(dao.index());
                                    Log.e("addPost", "Request Success!");
                                } catch (SQLiteConstraintException e) {
                                    Log.e("Database Error", "UNIQUE constraint failed: Post.pid", e);
                                }
                            }
                        });
                    } else if (response.code() == 401) {
                    Toast.makeText(context, "Unauthorized action",
                            Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 500) {
                        Toast.makeText(context, "Internal Server error",
                                Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 404) {
                        Toast.makeText(context, "User not found",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {
                    // Handle failure
                    String errorMessage = "Failed to send request";
                    Log.e("Request Failure", errorMessage, t);
                }
            });
        }

        public void delete(String publisher, String postId) {
            Call<Post> call = webServiceAPI.deletePost(publisher, postId);
            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    if (response.isSuccessful()) {
                        executor.execute(() -> {
                            synchronized (PostAPI.class) {
                                dao.delete(response.body());
                                postListData.postValue(dao.index());
                                Log.e("deletePost", "post deleted successfully!");
                            }
                        });
                    } else if (response.code() == 404) {
                        Toast.makeText(context, "Post not found",
                                Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 500) {
                        Toast.makeText(context, "Internal Server error",
                                Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 403) {
                        Toast.makeText(context, "Unauthorized",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {
                    String errorMessage = "Failed to send request";
                    Log.e("Request Failure", errorMessage, t);
                }
            });
        }

        public void update(String id, String pid, Post post) {
                Call<Post> call = webServiceAPI.updatePost(id, pid, post);
                call.enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        if (response.isSuccessful()) {
                            executor.execute(() -> {
                                synchronized (PostAPI.class) {
                                    dao.update(response.body());
                                    postListData.postValue(dao.index());
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        // Handle failure
                    }
                });
        }

        public void updateLikes(String id, String pid, Post post) {
            Call<Post> call = webServiceAPI.updateLikes(id, pid, post);
            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    if (response.isSuccessful()) {
                        executor.execute(() -> {
                            synchronized (PostAPI.class) {
                                dao.update(response.body());
                                postListData.postValue(dao.index());
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {
                    // Handle failure
                }
            });
        }



        public void fetchUsersPosts(String userId) {
            Call<List<Post>> call = webServiceAPI.getUsersPosts(userId);
            call.enqueue(new Callback<List<Post>>() {
                @Override
                public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                    if (response.isSuccessful()) {
                        executor.execute(() -> {
                            synchronized (PostAPI.class) {
                                dao.deleteAll();
                                dao.insert(response.body());
                                postListData.postValue(response.body());
                                Log.e("UserRepository", "Fetched friends' posts successfully");
                            }
                        });
                    }
                }
                @Override
                public void onFailure(Call<List<Post>> call, Throwable t) {
                    // Handle the case where the API call failed to be executed
                    Log.e("UserRepository", "Failed to fetch friends' posts", t);
                }
            });
        }
        public void clear() {
            executor.execute(() -> {
                synchronized (PostAPI.class) {
                    dao.deleteAll();
                    postListData.postValue(dao.index());
                }
            });
        }
    }