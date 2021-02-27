package com.rental.transport.service;

import com.burgstaller.okhttp.AuthenticationCacheInterceptor;
import com.burgstaller.okhttp.CachingAuthenticatorDecorator;
import com.burgstaller.okhttp.digest.CachingAuthenticator;
import com.burgstaller.okhttp.digest.Credentials;
import com.burgstaller.okhttp.digest.DigestAuthenticator;
import com.rental.transport.network.CalendarApi;
import com.rental.transport.network.CustomerApi;
import com.rental.transport.network.ImageApi;
import com.rental.transport.network.OrderApi;
import com.rental.transport.network.ParkingApi;
import com.rental.transport.network.RegistrationApi;
import com.rental.transport.network.TransportApi;
import com.rental.transport.network.TypeApi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    private static final String BASE_URL = "http://138.124.187.10:8080";
    private static NetworkService mInstance;
    private Retrofit mRetrofit;
    private Retrofit mRetrofitDidest;

    public static NetworkService getInstance(String account, String password) {
        mInstance = new NetworkService(account, password);
        return mInstance;
    }

    public static NetworkService getInstance() {

        if (mInstance == null) {
            mInstance = new NetworkService();
        }

        return mInstance;
    }

    private NetworkService() {

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private NetworkService(String account, String password) {

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final DigestAuthenticator authenticator = new DigestAuthenticator(new Credentials(account, password));
        final Map<String, CachingAuthenticator> authCache = new ConcurrentHashMap<>();

        mRetrofitDidest = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .authenticator(new CachingAuthenticatorDecorator(authenticator, authCache))
                        .addInterceptor(new AuthenticationCacheInterceptor(authCache))
                        .build()
                )
                .build();
    }

    public RegistrationApi getRegistrationApi() {
        return mRetrofit.create(RegistrationApi.class);
    }

    public CustomerApi getCustomerApi() {
        return mRetrofitDidest.create(CustomerApi.class);
    }

    public TransportApi getTransportApi() {
        return mRetrofitDidest.create(TransportApi.class);
    }

    public OrderApi getOrderApi() {
        return mRetrofitDidest.create(OrderApi.class);
    }

    public ParkingApi getParkingApi() {
        return mRetrofitDidest.create(ParkingApi.class);
    }

    public TypeApi getTypeApi() {
        return mRetrofitDidest.create(TypeApi.class);
    }

    public ImageApi getImageApi() {
        return mRetrofitDidest.create(ImageApi.class);
    }

    public CalendarApi getCalendarApi() {
        return mRetrofitDidest.create(CalendarApi.class);
    }
}
