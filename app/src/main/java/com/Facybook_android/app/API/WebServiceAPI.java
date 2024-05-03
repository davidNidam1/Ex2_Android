package com.Facybook_android.app.API;

import com.Facybook_android.app.Model.entities.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WebServiceAPI {
    @GET("posts")
    Call<List<Post>> getPosts();
    // Retrieve a specific post by ID
    @GET("posts/{id}")
    Call<Post> getPostById(@Path("id") int id);
    @POST("posts")
    Call<Void> createPost(@Body Post post);
    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") int id);
    // Update post method using PUT
    @PUT("posts/{id}")
    Call<Void> updatePost(@Path("id") int id, @Body Post post);
}

