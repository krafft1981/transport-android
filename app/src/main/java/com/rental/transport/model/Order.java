package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
    private Set<Property> property = new HashSet<>();
    @SerializedName("driver")
    private Set<Customer> driver = new HashSet<>();
    @SerializedName("event")
    private Set<Calendar> calendar = new HashSet<>();
    @SerializedName("message")
    private Set<Message> message = new HashSet<>();
    @SerializedName("created_at")
    private Integer createdAt;
    @SerializedName("state")
    private String state;
}
