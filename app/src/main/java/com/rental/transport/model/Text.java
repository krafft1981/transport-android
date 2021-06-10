package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Text {

    @SerializedName("message")
    private String message;
}
