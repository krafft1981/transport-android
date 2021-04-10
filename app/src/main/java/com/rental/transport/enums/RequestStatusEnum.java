package com.rental.transport.enums;

import lombok.Getter;

public enum RequestStatusEnum {

    REQUEST_NEW(1, "New"),
    REQUEST_ACCEPTED(2, "Accepted"),
    REQUEST_REJECTED(3, "Rejected"),
    REQUEST_EXPIRED(4, "Expired");

    @Getter
    private int id = 0;
    @Getter
    private String name;

    RequestStatusEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
