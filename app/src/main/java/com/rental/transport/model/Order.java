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
}
