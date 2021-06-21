package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Calendar {

    @SerializedName("id")
    private Long id;
    @SerializedName("day")
    private Long day;
    @SerializedName("hours")
    private List<Integer> hours;
    @SerializedName("note")
    private String note = "";

    public Calendar(Long day, List<Integer> hours) {
        setDay(day);
        setHours(hours);
    }
}
