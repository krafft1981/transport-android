package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class Parking {

    @SerializedName("id")
    private Long id;

    @SerializedName("property")
    private Set<Property> property = new HashSet<>();
    @SerializedName("image")
    private Set<Long> image = new HashSet<>();
    @SerializedName("customer")
    private Set<Long> customer = new HashSet<>();
    @SerializedName("transport")
    private Set<Long> transport = new HashSet<>();
}
