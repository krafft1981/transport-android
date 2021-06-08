package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Order {

    @SerializedName("id")
    private Long id;
    @SerializedName("day")
    private Long day;
    @SerializedName("hours")
    private Integer[] hours;
    @SerializedName("property")
    private List<Property> property = new ArrayList<>();
    @SerializedName("message")
    private List<Message> message = new ArrayList<>();

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
            if (max < value) max = value;
        }

        max++;
        return max;
    }
}
