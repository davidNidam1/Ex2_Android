package com.Facybook_android.app.API;

import com.Facybook_android.app.Model.entities.Post;
import com.Facybook_android.app.Model.entities.User;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WebServiceAPI {
    @GET("api/posts")
    Call<List<Post>> getPosts();
    @POST("api/posts")
    Call<Void> createPost(@Body Post post);
    @DELETE("api/users/{id}")
    Call<Void> deleteUser(@Path("id") int id);
    @DELETE("api/posts/{id}")
    Call<Void> deletePost(@Path("id") String id);
    @PUT("api/posts/{id}")
    Call<Void> updatePost(@Path("id") String id, @Body Post post);
    @POST("api/users")
    Call<Void> createUser(@Body User user);
    @POST("api/tokens")
    Call<Void> createToken(@Body String token);
    @GET("api/users/{id}")
    Call<User> getUser(@Path("id") String name);
    @PUT("api/users/{id}")
    Call<Void> updateUser(@Path("id") int id, @Body User user);
}

