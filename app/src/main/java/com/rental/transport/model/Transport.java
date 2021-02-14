package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
public class Transport {

    @SerializedName("id")
    private Long id;
    @SerializedName("type")
    private Type type;
    @SerializedName("property")
    private Set<Property> property = new HashSet<>();
    @SerializedName("parking")
    private Set<Long> parking = new HashSet<>();
    @SerializedName("customer")
    private Set<Long> customer = new HashSet<>();
    @SerializedName("image")
    private Set<Long> image = new HashSet<>();
}
