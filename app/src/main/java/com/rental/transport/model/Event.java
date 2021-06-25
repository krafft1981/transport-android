package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Event {

    @SerializedName("calendar")
    private Calendar calendar;
    @SerializedName("object_id")
    private Long objectId = null;
    @SerializedName("type")
    private Integer type;
}
