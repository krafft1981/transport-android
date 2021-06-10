package com.rental.transport.enums;

import lombok.Getter;

public enum EventTypeEnum {

    UNCKNOWN(0, "Uncknown"),
    GENERATED(1, "Generated"),
    NOTEBOOK(2, "NoteBook"),
    REQUEST(3, "Request"),
    ORDER(4, "Order"),
    BUSY(5, "Busy"),
    FREE(6, "Free");

    @Getter
    private Integer id = 0;

    @Getter
    private String name;

    EventTypeEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public static EventTypeEnum byId(Integer type) {
        for (EventTypeEnum result : EventTypeEnum.values()) {
            if (result.getId() == type)
                return result;
        }

        throw new IllegalArgumentException("Try get EventTypeEnum undefined value: " + type);
    }
}
