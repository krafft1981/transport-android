package com.rental.transport.network;

import com.rental.transport.model.Calendar;
import com.rental.transport.model.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface OrderApi {

    @Headers("Content-Type: application/json")
    @POST("/order/request/confirm")
    public Call<Void> doPostConfirmOrder(
            @Query("request_id") Long id
    );

    @Headers("Content-Type: application/json")
    @POST("/order/request/reject")
    public Call<Void> doPostRejectOrder(
            @Query("request_id") Long id
    );

    @Headers("Content-Type: application/json")
    @POST("/order")
    public Call<Void> doPostRequest(
            @Query("transport_id") Long transportId,
            @Query("day") Long day,
            @Query("start_at") Long startAt,
            @Query("stop_at") Long stopAt
    );

    @Headers("Content-Type: application/json")
    @GET("/order/transport")
    public Call<List<Event>> doGetEventByTransport(
            @Query("page") Integer page,
            @Query("size") Integer size
    );

    @Headers("Content-Type: application/json")
    @GET("/order/customer")
    public Call<List<Event>> doGetEventByCustomer(
            @Query("page") Integer page,
            @Query("size") Integer size
    );

    @Headers("Content-Type: application/json")
    @GET("/order/request/transport")
    public Call<List<Event>> doGetRequestEventByTransport(
            @Query("page") Integer page,
            @Query("size") Integer size
    );

    @Headers("Content-Type: application/json")
    @GET("/order/request/customer")
    public Call<List<Event>> doGetRequestEventByCustomer(
            @Query("page") Integer page,
            @Query("size") Integer size
    );

    @Headers("Content-Type: application/json")
    @PUT("/order/absent")
    public Call<List<Event>> doPutAbsentCustomer(
            @Query("day") Long day,
            @Query("start") Long start,
            @Query("stop") Long stop
    );

    @Headers("Content-Type: application/json")
    @DELETE("/order/absent")
    public Call<List<Event>> doDeleteAbsentCustomer(
            @Query("id") Long id
    );

    @Headers("Content-Type: application/json")
    @GET("/order/calendar/transport")
    public Call<List<Calendar>> doGetTransportCalendar(
            @Query("day") Long day,
            @Query("transport_id") Long transportId
    );

    @Headers("Content-Type: application/json")
    @GET("/order/calendar/customer")
    public Call<List<Event>> doGetCustomerCalendar(
            @Query("day") Long day
    );
}
