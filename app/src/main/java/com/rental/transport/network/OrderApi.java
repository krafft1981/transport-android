package com.rental.transport.network;

import com.rental.transport.model.Order;
import com.rental.transport.model.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OrderApi {

    @Headers("Content-Type: application/json")
    @GET("/order/customer")
    Call<List<Order>> doGetOrderByCustomer();

    @Headers("Content-Type: application/json")
    @GET("/order/transport")
    Call<List<Order>> doGetOrderByTransport();

    @Headers("Content-Type: application/json")
    @GET("/order/driver")
    Call<List<Order>> doGetOrderByDriver();

    @Headers("Content-Type: application/json")
    @GET("/order")
    Call<Order> doGetOrder(
            @Query("id") Long id
    );

    @Headers("Content-Type: application/json")
    @POST("/order/message")
    Call<Order> doPostMessage(
            @Query("order_id") Long orderId,
            @Body Text body
    );
}
