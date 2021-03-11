package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Calendar {

    @SerializedName("id")
    private Long id;
    @SerializedName("customer")
    private Long customer;
    @SerializedName("start_at")
    private Long startAt;
    @SerializedName("stop_at")
    private Long stopAt;
    @SerializedName("order")
    private Long order;
    @SerializedName("type")
    private Long type;
}
