package com.rental.transport.model;

import lombok.Data;

@Data
public class Message {

    private String text;
    private Customer customer;
    private Long date;
}
