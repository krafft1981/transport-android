package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Order {

    @SerializedName("id")
    private Long id;
    @SerializedName("customer")
    private Long customer;
    @SerializedName("transport")
    private Transport transport;
    @SerializedName("property")
    private List<Property> property = new ArrayList<>();
    @SerializedName("driver")
    private List<Customer> driver = new ArrayList<>();
    @SerializedName("event")
    private List<Calendar> calendar = new ArrayList<>();
    @SerializedName("message")
    private List<Message> message = new ArrayList<>();
    @SerializedName("created_at")
    private Long createdAt;
    @SerializedName("state")
    private String state;
}
