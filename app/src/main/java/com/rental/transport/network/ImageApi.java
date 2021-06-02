package com.rental.transport.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ImageApi {

    @Headers("Content-Type: text/plain")
    @POST("/image")
    Call<Long> doPostImage(
            @Body byte[] data
    );

    @Headers("Content-Type: application/json")
    @GET("/image")
    Call<byte[]> doGetImage(
            @Query("id") Long id
    );

    @DELETE("/image")
    Call<Void> doDeleteImage(
            @Query("id") Long[] id
    );

    @Headers("Content-Type: application/json")
    @GET("/image")
    Call<byte[]> doGetUriImage(
            @Path("user") Long id
    );

    @Headers("Content-Type: application/json")
    @GET("/image/list")
    Call<List<Long>> doGetImageList(
            @Query("page") Integer page,
            @Query("size") Integer size
    );
}
