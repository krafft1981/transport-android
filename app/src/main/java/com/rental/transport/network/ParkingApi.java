package com.rental.transport.network;

import com.rental.transport.model.Parking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ParkingApi {

    @Headers("Content-Type: application/json")
    @DELETE("/parking")
    Call<Void> doDeleteParking(
            @Query("id") Long id
    );

    @Headers("Content-Type: application/json")
    @GET("/parking")
    Call<Parking> doGetParking(
            @Query("id") Long id
    );

    @GET("/parking/list")
    Call<List<Parking>> doGetParkingList(
            @Query("page") Integer page,
            @Query("size") Integer size);

    @Headers("Content-Type: application/json")
    @POST("/parking")
    Call<Long> doPostParking();

    @Headers("Content-Type: application/json")
    @PUT("/parking")
    Call<Void> doPutParking(
            @Body Parking parking
    );

    @Headers("Content-Type: application/json")
    @GET("/parking/count")
    Call<Long> doGetCountParking();

    @Headers("Content-Type: application/json")
    @POST("/parking/image")
    Call<Parking> doPostParkingImage(
            @Query("parking_id") Long id,
            @Body byte[] data
    );

    @Headers("Content-Type: application/json")
    @DELETE("/parking/image")
    Call<Parking> doDeleteParkingImage(
            @Query("parking_id") Long parkingId,
            @Query("image_id") Long imageId
    );
}
