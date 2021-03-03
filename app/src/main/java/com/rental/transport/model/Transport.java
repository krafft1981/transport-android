package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Transport {

    @SerializedName("id")
    private Long id;
    @SerializedName("type")
    private Type type;
    @SerializedName("property")
    private List<Property> property = new ArrayList<>();
    @SerializedName("parking")
    private List<Long> parking = new ArrayList<>();
    @SerializedName("customer")
    private List<Long> customer = new ArrayList<>();
    @SerializedName("image")
    private List<Long> image = new ArrayList<>();
}
