package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.Set;

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
    private Set<Property> property = new HashSet<>();
    @SerializedName("image")
    private Set<Long> image = new HashSet<>();
    @SerializedName("transport")
    private Set<Long> transport = new HashSet<>();
    @SerializedName("parking")
    private Set<Long> parking = new HashSet<>();
}
