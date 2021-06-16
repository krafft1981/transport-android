package com.rental.transport.network;

import com.rental.transport.model.Event;
import com.rental.transport.model.Request;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RequestApi {

    @Headers("Content-Type: application/json")
    @POST("/request/confirm")
    Call<List<Request>> doPostConfirmRequest(
            @Query("request_id") Long requestId
    );

    @Headers("Content-Type: application/json")
    @POST("/request/reject")
    Call<List<Request>> doPostRejectRequest(
            @Query("request_id") Long requestId
    );

    @Headers("Content-Type: application/json")
    @POST("/request")
    Call<Map<Integer, Event>> doPostRequest(
            @Query("transport_id") Long transportId,
            @Query("day") Long day,
            @Query("hour") Integer[] hour
    );

    @Headers("Content-Type: application/json")
    @POST("/request/customer")
    Call<List<Request>> doGetRequestAsCustomer(

    );

    @Headers("Content-Type: application/json")
    @POST("/request/driver")
    Call<List<Request>> doGetRequestAsDriver(

    );
}
