package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class Calendar {

    @SerializedName("day")
    private Long day;
    @SerializedName("hours")
    private List<Integer> hours;
    @SerializedName("note")
    private String note = "";
}
