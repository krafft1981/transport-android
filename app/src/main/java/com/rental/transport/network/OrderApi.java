package com.rental.transport.network;

import com.rental.transport.model.Event;
import com.rental.transport.model.Order;
import com.rental.transport.model.Request;
import com.rental.transport.model.Text;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface OrderApi {

    @Headers("Content-Type: application/json")
    @PUT("/order/absent")
    public Call<Event> doPutAbsentCustomer(
            @Query("id") Long id,
            @Body String message
    );

    @Headers("Content-Type: application/json")
    @POST("/order/absent")
    public Call<Event> doPostAbsentCustomer(
            @Query("day") Long day,
            @Query("hour") Integer[] hour
    );

    @Headers("Content-Type: application/json")
    @DELETE("/order/absent")
    public Call<List<Event>> doDeleteAbsentCustomer(
            @Query("id") Long id
    );

    @Headers("Content-Type: application/json")
    @GET("/order/calendar/transport")
    public Call<Map<Integer, Event>> doGetTransportCalendar(
            @Query("day") Long day,
            @Query("transport_id") Long transportId
    );

    @Headers("Content-Type: application/json")
    @GET("/order/calendar/customer")
    public Call<Map<Integer, Event>> doGetCustomerCalendar(
            @Query("day") Long day
    );

    @Headers("Content-Type: application/json")
    @GET("/order/calendar/driver")
    public Call<Map<Integer, Event>> doGetDriverCalendar(
            @Query("day") Long day
    );

    @Headers("Content-Type: application/json")
    @POST("/order/request/confirm")
    public Call<List<Request>> doPostConfirmOrder(
            @Query("request_id") Long id
    );

    @Headers("Content-Type: application/json")
    @POST("/order/request/reject")
    public Call<List<Request>> doPostRejectOrder(
            @Query("request_id") Long id
    );

    @Headers("Content-Type: application/json")
    @POST("/order/request")
    public Call<Map<Integer, Event>> doPostRequest(
            @Query("transport_id") Long transportId,
            @Query("day") Long day,
            @Query("hour") Integer[] hour
    );

    @Headers("Content-Type: application/json")
    @GET("/order/customer")
    public Call<List<Order>> doGetOrderByCustomer();

    @Headers("Content-Type: application/json")
    @GET("/order/transport")
    public Call<List<Order>> doGetOrderByTransport();

    @Headers("Content-Type: application/json")
    @GET("/order/driver")
    public Call<List<Order>> doGetOrderByDriver();

    @Headers("Content-Type: application/json")
    @GET("/event/customer")
    public Call<Map<Integer, Event>> doGetEventByCustomer();

    @Headers("Content-Type: application/json")
    @GET("/order/request/driver")
    public Call<List<Request>> doGetRequestAsDriver();

    @Headers("Content-Type: application/json")
    @GET("/order/request/customer")
    public Call<List<Request>> doGetRequestAsCustomer();

    @Headers("Content-Type: application/json")
    @POST("/order/message")
    public Call<Order> doPostOrderMessage(
            @Query("order_id") Long id,
            @Body Text body
    );

    @Headers("Content-Type: application/json")
    @GET("/order")
    public Call<Order> doGetOrder(
            @Query("id") Long id
    );
}
