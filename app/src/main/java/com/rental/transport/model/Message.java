package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import lombok.Data;

@Data
public class Message {

    @SerializedName("id")
    private Long id;
    @SerializedName("text")
    private String text;
    @SerializedName("customer_id")
    private Long customerId;
    @SerializedName("date")
    private Date date;
}
