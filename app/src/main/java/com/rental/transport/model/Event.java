package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Event {

    @SerializedName("calendar")
    private Calendar calendar;
    @SerializedName("order")
    private Order order;
    @SerializedName("request")
    private Request request;
    @SerializedName("type")
    private Integer type;
    @SerializedName("note")
    private NoteBook noteBook;
}
