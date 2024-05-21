package com.Facybook_android.app.API;

import com.Facybook_android.app.Model.entities.Post;
import com.Facybook_android.app.Model.entities.Token;
import com.Facybook_android.app.Model.entities.User;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WebServiceAPI {
    @GET("api/posts")
    Call<List<Post>> getPosts();
    @POST("api/users/{id}/posts")
    Call<Post> createPost(@Path("id") String id, @Body Post post);
    @GET("api/users/{id}/posts")
    Call<List<Post>> getUsersPosts(@Path("id") String id);
    @DELETE("api/users/{id}")
    Call<Void> deleteUser(@Path("id") String id);
    @DELETE("api/users/{id}/posts/{pid}")
    Call<Post> deletePost(@Path("id") String id, @Path("pid") String pid);
    @PATCH("api/users/{id}/posts/{pid}")
    Call<Post> updatePost(@Path("id") String id, @Path("pid") String pid, @Body RequestBody body);
    @POST("api/users")
    Call<Void> createUser(@Body User user);
    @POST("api/tokens")
    Call<Token> getToken(@Body User user);
    @GET("api/users/{id}")
    Call<User> getUser(@Path("id") String name);
    @PATCH("api/users/{id}")
    Call<User> updateUser(@Path("id") String name, @Body User user);
    @GET("api/users/{id}/friends")
    Call<List<String>> getFriends(@Path("id") String id);
    @POST("api/users/{id}/friends")
    Call<Void> sendRequest(@Path("id") String id);
    @PATCH("api/users/{id}/friends/{fid}")
    Call<User> acceptRequest(@Path("id") String id, @Path("fid") String fid);
    @DELETE("api/users/{id}/friends/{fid}")
    Call<User> denyRequest(@Path("id") String id, @Path("fid") String fid);
}

