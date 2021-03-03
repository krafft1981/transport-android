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
    @GET("/customer/count")
    public Call<Long> doGetCountCustomerRequest();

    @Headers("Content-Type: application/json")
    @PUT("/customer")
    public Call<Void> doPutUpdateCustomerRequest(
            @Body Customer customer
    );

    @Headers("Content-Type: application/json")
    @GET("/customer/list")
    public Call<List<Customer>> doGetPagesCustomerRequest(
            @Query("page") Integer page,
            @Query("size") Integer size
    );

    @Headers("Content-Type: application/json")
    @GET("/customer")
    public Call<List<Customer>> doGetCount();

    @Headers("Content-Type: application/json")
    @GET("/customer")
    public Call<Customer> doGetCustomerRequest();

    @Headers("Content-Type: application/json")
    @PUT("/customer/update/password")
    public Call<Void> doPutUpdateCustomerPasswordRequest(
            @Query("password") String password
    );
}
