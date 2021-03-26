package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Calendar {

    @SerializedName("id")
    private Long id;
    @SerializedName("day_num")
    private Long dayNum;
    @SerializedName("start_at")
    private Long startAt;
    @SerializedName("stop_at")
    private Long stopAt;
}
