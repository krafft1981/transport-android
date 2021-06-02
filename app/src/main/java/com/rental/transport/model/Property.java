package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Property {

    @SerializedName("human_name")
    private String humanName;
    @SerializedName("logic_name")
    private String logicName;
    @SerializedName("value")
    private String value;
    @SerializedName("type")
    private String type;
    @SerializedName("order")
    private Integer order;
}
