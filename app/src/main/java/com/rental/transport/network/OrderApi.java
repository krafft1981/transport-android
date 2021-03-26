package com.rental.transport.network;

import com.rental.transport.model.Calendar;
import com.rental.transport.model.Customer;
import com.rental.transport.model.Event;
import com.rental.transport.model.Order;

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
    @POST("/order")
    public Call<Void> doPostOrderRequest(
            @Query("transport_id") Long transportId,
            @Query("day") Long day,
            @Query("start_at") Long startAt,
            @Query("stop_at") Long stopAt
    );

    @Headers("Content-Type: application/json")
    @GET("/order/page")
    public Call<List<Order>> doGetPagesOrderRequest(
            @Query("page") Integer page,
            @Query("size") Integer size
    );

    @Headers("Content-Type: application/json")
    @GET("/order/request")
    public Call<List<Long>> doGetOrderRequest();

    @Headers("Content-Type: application/json")
    @GET("/order/client")
    public Call<List<Order>> doGetCustomerOrders(
            @Query("page") Integer page,
            @Query("size") Integer size
    );

    @Headers("Content-Type: application/json")
    @POST("/order/confirm")
    public Call<List<Customer>> doPostConfirmOrderRequest(
            @Query("order_id") Long id
    );

    @Headers("Content-Type: application/json")
    @POST("/order/reject")
    public Call<Customer> doPostRejectOrderRequest(
            @Query("order_id") Long id
    );

    @Headers("Content-Type: application/json")
    @GET("/order/calendar/transport")
    public Call<List<Calendar>> doGetCustomerTransportCalendar(
            @Query("transport_id") Long transportId,
            @Query("customer_id") Long customerId,
            @Query("day") Long day
    );

    @Headers("Content-Type: application/json")
    @GET("/order/calendar/customer")
    public Call<List<Event>> doGetCustomerCalendar(
            @Query("day") Long day
    );

    @Headers("Content-Type: application/json")
    @PUT("/order/calendar")
    public Call<Void> doPutOutRequest(
            @Query("day") Long day,
            @Query("start") Long start,
            @Query("stop") Long stop
    );

    @Headers("Content-Type: application/json")
    @DELETE("/order/calendar")
    public Call<Void> doDeleteOutRequest(
            @Query("day") Long day,
            @Query("start") Long start,
            @Query("stop") Long stop
    );
}
