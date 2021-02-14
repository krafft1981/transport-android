package com.rental.transport.model;

import java.util.Date;

import lombok.Data;

@Data
public class Message {

    private String text;
    private Customer customer;
    private Date date;
}
