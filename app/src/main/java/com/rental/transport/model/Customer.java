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
    @SerializedName("send_email")
    private Boolean sendEmail;
    @SerializedName("property")
    private List<Property> property = new ArrayList<>();
    @SerializedName("image")
    private List<Long> image = new ArrayList<>();
    @SerializedName("transport")
    private List<Long> transport = new ArrayList<>();
    @SerializedName("parking")
    private List<Long> parking = new ArrayList<>();
}
