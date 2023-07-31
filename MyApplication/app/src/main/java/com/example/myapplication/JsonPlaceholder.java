package com.example.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JsonPlaceholder {
    @POST("/character")
    public Call<Post> postData( @Body Request request );
    @GET("/tip")
    public Call<Post> getTip();

    @GET("/word")
    public Call<Post> getWord(
            @Query("length") int length
    );

    @GET("/tips")
    public Call<Post> getTips();

}



