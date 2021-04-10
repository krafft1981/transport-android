package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Event {

    @SerializedName("order")
    private Order order;
    @SerializedName("request")
    private Request request;
    @SerializedName("type")
    private Integer type;
}
