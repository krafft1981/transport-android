package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Order {

    @SerializedName("id")
    private Long id;

    @SerializedName("property")
    private List<Property> property = new ArrayList<>();
    @SerializedName("created_at")
    private Long createdAt;
    @SerializedName("confirmed_at")
    private Long confirmedAt;
    @SerializedName("status")
    private String status;
}
