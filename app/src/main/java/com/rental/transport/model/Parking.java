package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Parking {

    @SerializedName("id")
    private Long id;

    @SerializedName("property")
    private List<Property> property = new ArrayList<>();
    @SerializedName("image")
    private List<Long> image = new ArrayList<>();
    @SerializedName("customer")
    private List<Long> customer = new ArrayList<>();
    @SerializedName("transport")
    private List<Long> transport = new ArrayList<>();
}
