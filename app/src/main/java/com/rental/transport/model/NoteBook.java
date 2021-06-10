package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import lombok.Data;

@Data
public class NoteBook {

    @SerializedName("text")
    private String text;
    @SerializedName("customer_id")
    private Long customerId;
    @SerializedName("calendar_id")
    private Long calendar;
    @SerializedName("date")
    private Date date;
}
