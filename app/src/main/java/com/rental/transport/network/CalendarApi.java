package com.rental.transport.network;

import com.rental.transport.model.Event;
import com.rental.transport.model.Text;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CalendarApi {

    @Headers("Content-Type: application/json")
    @GET("/calendar/customer")
    Call<List<Event>> doGetCustomerCalendar(
            @Query("day") Long day
    );

    @Headers("Content-Type: application/json")
    @GET("/calendar/transport")
    Call<List<Event>> doGetTransportCalendar(
            @Query("transport_id") Long transportId,
            @Query("day") Long day
    );

    @Headers("Content-Type: application/json")
    @POST("/calendar/note")
    Call<List<Event>> doPostCalendarNote(
            @Query("hour") Integer[] hour,
            @Query("day") Long day,
            @Body Text body
    );

    @Headers("Content-Type: application/json")
    @PUT("/calendar/note")
    Call<List<Event>> doPutCalendarNote(
            @Query("calendar_id") Long calendarId,
            @Body Text body
    );

    @Headers("Content-Type: application/json")
    @DELETE("/calendar/note")
    Call<List<Event>> doDeleteCalendarNote(
            @Query("calendar_id") Long calendarId
    );
}
