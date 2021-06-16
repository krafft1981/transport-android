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
    Call<Void> doPutUpdateCustomerRequest(
            @Body Customer customer
    );

    @Headers("Content-Type: application/json")
    @GET("/customer/list")
    Call<List<Customer>> doGetPagesCustomerRequest(
            @Query("page") Integer page,
            @Query("size") Integer size
    );

    @Headers("Content-Type: application/json")
    @GET("/customer")
    Call<List<Customer>> doGetCount();

    @Headers("Content-Type: application/json")
    @GET("/customer")
    Call<Customer> doGetCustomerRequest();

    @Headers("Content-Type: application/json")
    @PUT("/customer/update/password")
    Call<Void> doPutUpdateCustomerPasswordRequest(
            @Query("password") String password
    );
}
