package com.rental.transport.network;

import com.rental.transport.model.Transport;

import java.util.Set;

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
    public Call<Void> doDeleteTransport(
            @Query("id") Long id
    );

    @GET("/transport/list")
    public Call<Set<Transport>> doGetTransportList(
            @Query("page") Integer page,
            @Query("size") Integer size
    );

    @GET("/transport/list/type")
    public Call<Set<Transport>> goGetTransportTyped(
            @Query("type") Long type,
            @Query("page") Integer page,
            @Query("size") Integer size
    );

    @Headers("Content-Type: application/json")
    @POST("/transport")
    public Call<Long> doPostTransport(
            @Query("type") String type
    );

    @Headers("Content-Type: application/json")
    @PUT("/transport")
    public Call<Void> doPutTransport(
            @Body Transport transport
    );

    @GET("/transport/count")
    public Call<Long> doGetTransportCount();

    @GET("/transport/parking")
    public Call<Set<Transport>> doGetParkingTransport(
            @Query("parking_id") Long parkingId
    );

    @GET("/transport/my")
    public Call<Set<Transport>> doGetMyTransport();
}
