package com.rental.transport.network;

import com.rental.transport.model.Customer;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RegistrationApi {

    @Headers("Content-Type: application/json")
    @POST("/registration")
    Call<Customer> doPostRegistration(
            @Query("account") String account,
            @Query("password") String password,
            @Query("phone") String phone,
            @Query("fio") String fio
    );

    @Headers("Content-Type: application/json")
    @POST("/registration/email")
    Call<Void> doPostEmailRegistration(
            @Query("account") String account
    );
}
