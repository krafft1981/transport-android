package com.rental.transport.network;

import com.rental.transport.model.Calendar;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CalendarApi {

    @Headers("Content-Type: application/json")
    @GET("/calendar/transport")
    public Call<List<Calendar>> doGetTransportCalendar(
            @Query("days") Long[] days,
            @Query("transport_id") Long transportId
    );

    @Headers("Content-Type: application/json")
    @GET("/calendar/customer")
    public Call<List<Calendar>> doGetCustomerCalendar(
            @Query("days") Long[] days
    );

    @Headers("Content-Type: application/json")
    @PUT("/calendar")
    public Call<Void> doPutOutRequest(
            @Query("day") Long day,
            @Query("start") Long start,
            @Query("stop") Long stop
    );

    @Headers("Content-Type: application/json")
    @DELETE("/calendar")
    public Call<Void> doDeleteOutRequest(
            @Query("day") Long day,
            @Query("start") Long start,
            @Query("stop") Long stop
    );
}
