package com.rental.transport.network;

import com.rental.transport.model.Customer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CustomerApi {

    @Headers("Content-Type: application/json")
    @PUT("/customer")
    public Call<Void> doPutCustomer(
            @Body Customer customer
    );

    @Headers("Content-Type: application/json")
    @GET("/customer/list")
    public Call<List<Customer>> doGetPagesCustomer(
            @Query("page") Integer page,
            @Query("size") Integer size
    );

    @Headers("Content-Type: application/json")
    @GET("/customer")
    public Call<Customer> doGetCustomer();

    @Headers("Content-Type: application/json")
    @PUT("/customer/update/password")
    public Call<Void> doPutUpdateCustomerPassword(
            @Query("password") String password
    );
}
