package com.rental.transport.network;

import com.rental.transport.model.Transport;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface TransportApi {

    @Headers("Content-Type: application/json")
    @DELETE("/transport")
    Call<Void> doDeleteTransport(
            @Query("id") Long id
    );

    @GET("/transport/list")
    Call<List<Transport>> doGetTransportList(
            @Query("page") Integer page,
            @Query("size") Integer size
    );

    @GET("/transport/list/type")
    Call<List<Transport>> goGetTransportTyped(
            @Query("type") Long type,
            @Query("page") Integer page,
            @Query("size") Integer size
    );

    @Headers("Content-Type: application/json")
    @POST("/transport")
    Call<Long> doPostTransport(
            @Query("type") String type
    );

    @Headers("Content-Type: application/json")
    @PUT("/transport")
    Call<Void> doPutTransport(
            @Body Transport transport
    );

    @GET("/transport/parking")
    Call<List<Transport>> doGetParkingTransport(
            @Query("parking_id") Long parkingId
    );

    @GET("/transport/my")
    Call<List<Transport>> doGetMyTransport();

    @Headers("Content-Type: application/json")
    @POST("/transport/image")
    Call<Transport> doPostTransportImage(
            @Query("transport_id") Long id,
            @Body byte[] data
    );

    @Headers("Content-Type: application/json")
    @DELETE("/transport/image")
    Call<Transport> doDeleteTransportImage(
            @Query("transport_id") Long transportId,
            @Query("image_id") Long imageId
    );
}
