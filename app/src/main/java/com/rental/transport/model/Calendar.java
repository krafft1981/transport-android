package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
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

    public Integer getMinHour() {
        Integer min = Integer.MAX_VALUE;

        for (Integer value : hours) {
            if (min > value)
                min = value;
        }

        return min;
    }

    public Integer getMaxHour() {
        Integer max = Integer.MIN_VALUE;

        for (Integer value : hours) {
            if (max < value)
                max = value;
        }

        return max;
    }
}
