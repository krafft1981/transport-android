package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Parking {

    @SerializedName("id")
    private Long id;
    @SerializedName("name")
    private String name;
    @SerializedName("locality")
    private String locality;
    @SerializedName("address")
    private String address;
    @SerializedName("latitude")
    private Double latitude;
    @SerializedName("longitude")
    private Double longitude;
    @SerializedName("description")
    private String description;
    @SerializedName("image")
    private List<Long> image = new ArrayList<>();
    @SerializedName("customer")
    private List<Long> customer = new ArrayList<>();
    @SerializedName("transport")
    private List<Long> transport = new ArrayList<>();
}
