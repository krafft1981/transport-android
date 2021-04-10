package com.rental.transport.enums;

import lombok.Getter;

public enum EventTypeEnum {

    GENERATED(1, "Generated"),
    UNAVAILABLE(2, "Unavailable"),
    REQUEST(3, "Request"),
    ORDER(4, "Order"),
    BUSY(5, "Busy"),
    FREE(6, "Free");

    @Getter
    private int id = 0;
    @Getter
    private String name;

    EventTypeEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
