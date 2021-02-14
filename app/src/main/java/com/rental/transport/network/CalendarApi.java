package com.rental.transport.network;

import com.rental.transport.model.Calendar;
import java.util.Set;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CalendarApi {

    @Headers("Content-Type: application/json")
    @GET("/event/count")
    public Call<Long> doGetCountRequest();

    @Headers("Content-Type: application/json")
    @GET("/event/transport")
    public Call<Set<Calendar>> doGetTransportCalendar(
            @Query("days") Long[] days,
            @Query("transport_id") Long transportId
    );

    @Headers("Content-Type: application/json")
    @GET("/event/customer")
    public Call<Set<Calendar>> doGetCustomerCalendar(
            @Query("days") Long[] days
    );

    @Headers("Content-Type: application/json")
    @PUT("/event")
    public Call<Void> doPutOutRequest(
            @Query("day") Integer day,
            @Query("start") Integer start,
            @Query("stop") Integer stop
    );

    @Headers("Content-Type: application/json")
    @DELETE("/event")
    public Call<Void> doDeleteOutRequest(
            @Query("start") Integer start,
            @Query("stop") Integer stop
    );
}
