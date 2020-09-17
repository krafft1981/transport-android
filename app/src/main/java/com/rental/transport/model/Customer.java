package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Customer {

    @SerializedName("id")
    private Long id;
    @SerializedName("account")
    private String account;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("family")
    private String family;
    @SerializedName("phone")
    private String phone;
    @SerializedName("startWorkAt")
    private Integer startWorkAt;
    @SerializedName("stopWorkAt")
    private Integer stopWorkAt;
    @SerializedName("workAtWeekEnd")
    private Boolean workAtWeekEnd;
    @SerializedName("image")
    private List<Long> image = new ArrayList<>();
    @SerializedName("transport")
    private List<Long> transport = new ArrayList<>();
    @SerializedName("parking")
    private List<Long> parking = new ArrayList<>();
}
