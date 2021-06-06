package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

public class Request {

    @SerializedName("id")
    private Long id;
    @SerializedName("created_at")
    private Long createdAt;
    @SerializedName("interact_at")
    private Long interactAt;
    @SerializedName("order")
    private Long order;
    @SerializedName("customer")
    private Customer customer;
    @SerializedName("driver")
    private Customer driver;
    @SerializedName("transport")
    private Transport transport;
    @SerializedName("day")
    private Long day;
    @SerializedName("hours")
    private Integer[] hours;
}
