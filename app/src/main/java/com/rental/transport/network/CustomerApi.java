package com.rental.transport.network;

import com.rental.transport.model.Customer;
import com.rental.transport.model.Transport;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CustomerApi {

    @Headers("Content-Type: application/json")
    @PUT("/customer")
    Call<Customer> doPutCustomer(
            @Body Customer customer
    );

    @Headers("Content-Type: application/json")
    @GET("/customer/list")
    Call<List<Customer>> doGetPagesCustomer(
            @Query("page") Integer page,
            @Query("size") Integer size
    );

    @Headers("Content-Type: application/json")
    @GET("/customer")
    Call<Customer> doGetCustomer();

    @Headers("Content-Type: application/json")
    @PUT("/customer/update/password")
    Call<Void> doPutUpdateCustomerPassword(
            @Query("password") String password
    );

    @Headers("Content-Type: application/json")
    @POST("/customer/image")
    Call<Customer> doAddCustomerImage(
            @Body byte[] data
    );

    @Headers("Content-Type: application/json")
    @DELETE("/customer/image")
    Call<Customer> doDropCustomerImage(
            @Query("image_id") Long imageId
    );
}
