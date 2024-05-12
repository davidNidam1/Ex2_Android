    package com.Facybook_android.app.API;

    import static com.Facybook_android.app.Context.MyApplication.context;

    import android.content.Context;
    import android.content.SharedPreferences;
    import android.util.Log;
    import android.widget.Toast;

    import androidx.lifecycle.MutableLiveData;

    import com.Facybook_android.app.Model.entities.Post;
    import com.Facybook_android.app.Repository.interfaces.PostDao;
    import com.Facybook_android.app.Context.MyApplication;
    import com.Facybook_android.app.R;

    import java.util.List;

    import okhttp3.OkHttpClient;
    import okhttp3.Request;
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
                         new Thread(() -> {
                            dao.deleteAll();
                            dao.insert(response.body());
                            postListData.postValue(response.body());
                            }).start();
                        }
                @Override
                public void onFailure(Call<List<Post>> call, Throwable t) {}
            });
        }

        public void add(Post post, String id) {
            Call<Void> call = webServiceAPI.createPost(id, post);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    // After adding to the server, add to the local database
                    if (response.isSuccessful()) {
                        new Thread(() -> {
                            dao.insert(post);
                            postListData.postValue(dao.index());
                            Log.e("addPost", "Request Success!");
                        }).start();
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

        public void delete(String publisher, int postId) {
            Call<Post> call = webServiceAPI.deletePost(publisher, postId);
            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    if (response.isSuccessful()) {
                        new Thread(() -> {
                            dao.delete(response.body());
                            postListData.postValue(dao.index());
                            Log.e("deletePost", "post deleted successfully!");
                        }).start();
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

        public void update(Post post) {
//            Call<Void> call = webServiceAPI.updatePost(post.getId(), post);
//            call.enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, Response<Void> response) {
//                    if (response.isSuccessful()) {
//                        new Thread(() -> {
//                            dao.update(post);
//                            postListData.postValue(dao.index());
//                        }).start();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    // Handle failure
//                }
//            });
        }

        public void fetchUsersPosts(String userId) {
            Call<List<Post>> call = webServiceAPI.getUsersPosts(userId);
            call.enqueue(new Callback<List<Post>>() {
                @Override
                public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                    if (response.isSuccessful()) {
                        new Thread(() -> {
                            dao.deleteAll();
                            dao.insert(response.body());
                            postListData.postValue(response.body());
                            Log.e("UserRepository", "Fetched friends' posts successfully");
                        }).start();
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
            new Thread (() -> {
                dao.deleteAll();
                postListData.postValue(dao.index());
            }).start();
        }
    }