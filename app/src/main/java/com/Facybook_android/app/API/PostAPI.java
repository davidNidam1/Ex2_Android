    package com.Facybook_android.app.API;

    import static com.Facybook_android.app.Context.MyApplication.context;

    import android.util.Log;
    import android.widget.Toast;

    import androidx.lifecycle.MutableLiveData;

    import com.Facybook_android.app.Model.entities.Post;
    import com.Facybook_android.app.Repository.interfaces.PostDao;
    import com.Facybook_android.app.Context.MyApplication;
    import com.Facybook_android.app.R;

    import java.util.List;

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

            retrofit = new Retrofit.Builder()
            .baseUrl(context.getString(R.string.BaseUrl))
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
                            dao.deleteAll();
                            dao.insert(response.body());
                            postListData.postValue(dao.index());
                            }).start();
                        }
                @Override
                public void onFailure(Call<List<Post>> call, Throwable t) {}
            });
        }

        public void add(Post post) {
            Call<Void> call = webServiceAPI.createPost(post);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    // After adding to the server, add to the local database
                    if (response.isSuccessful()) {
                        new Thread(() -> {
                            dao.insert(post);
                            postListData.postValue(dao.index());
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

        public void delete(Post post) {
//            Call<Void> call = webServiceAPI.deletePost(post.getId());
//            call.enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, Response<Void> response) {
//                    if (response.isSuccessful()) {
//                        new Thread(() -> {
//                            dao.delete(post);
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
    }