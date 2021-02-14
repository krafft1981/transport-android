package com.rental.transport.network;

import com.rental.transport.model.Customer;
import com.rental.transport.model.Order;
import java.util.Set;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OrderApi {

    @Headers("Content-Type: application/json")
    @POST("/order")
    public Call<Long> doPostOrderRequest(
            @Query("transport_id") Long transportId,
            @Query("calendar_id") Long[] calendarIds
    );

    @Headers("Content-Type: application/json")
    @GET("/order/page")
    public Call <Set<Order>> doGetPagesOrderRequest(
            @Query("page") Integer page,
            @Query("size") Integer size
    );

    @Headers("Content-Type: application/json")
    @GET("/order/time")
    public Call <Set<Order>> doGetTimesOrderRequest(
            @Query("calendar_id") Long[] calendarIds
    );

    @Headers("Content-Type: application/json")
    @GET("/order/request")
    public Call<Set<Long>> doGetOrderRequest();

    @Headers("Content-Type: application/json")
    @GET("/order/client")
    public Call<Set<Order>> doGetCustomerOrders();

    @Headers("Content-Type: application/json")
    @POST("/order/confirm")
    public Call<Set<Customer>> doPostConfirmOrderRequest(
            @Query("order_id") Long id
    );

    @Headers("Content-Type: application/json")
    @POST("/order/reject")
    public Call<Customer> doPostRejectOrderRequest(
            @Query("order_id") Long id
    );
}
