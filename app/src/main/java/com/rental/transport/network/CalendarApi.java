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

public interface CalendarApi {

    @Headers("Content-Type: application/json")
    @PUT("/order/absent")
    Call<Event> doPutAbsentCustomer(
            @Query("id") Long id,
            @Body String message
    );

    Call<Map<Integer, Event>> doGetTransportCalendar(long time, Long id);
}
