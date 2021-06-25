package com.rental.transport.network;

import com.rental.transport.model.Event;
import com.rental.transport.model.Request;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RequestApi {

    @Headers("Content-Type: application/json")
    @POST("/request/confirm")
    Call<List<Event>> doPostConfirmRequest(
            @Query("request_id") Long requestId
    );

    @Headers("Content-Type: application/json")
    @POST("/request/reject")
    Call<List<Event>> doPostRejectRequest(
            @Query("request_id") Long requestId
    );

    @Headers("Content-Type: application/json")
    @POST("/request")
    Call<List<Event>> doPostRequest(
            @Query("transport_id") Long transportId,
            @Query("day") Long day,
            @Query("hour") Integer[] hour
    );

    @Headers("Content-Type: application/json")
    @GET("/request")
    Call<List<Request>> doGetRequest(
            @Query("id") Long[] ids
    );

    @Headers("Content-Type: application/json")
    @GET("/request/customer")
    Call<List<Event>> doGetRequestAsCustomer();

    @Headers("Content-Type: application/json")
    @GET("/request/driver")
    Call<List<Event>> doGetRequestAsDriver();
}
