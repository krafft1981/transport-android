package com.rental.transport.network;

import com.rental.transport.model.Event;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface CalendarApi {

    @Headers("Content-Type: application/json")
    @GET("/calendar/customer")
    Call<Map<Integer, Event>> doGetCustomerCalendar(
            @Query("day") Long day
    );

    @Headers("Content-Type: application/json")
    @GET("/calendar/transport")
    Call<Map<Integer, Event>> doGetTransportCalendar(
            @Query("transport_id") Long transportId,
            @Query("day") Long day
    );
}
