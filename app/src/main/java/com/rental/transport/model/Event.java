package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Event {

    @SerializedName("calendar")
    private Calendar calendar;
    @SerializedName("type")
    private Integer type;
}
