package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Property {

    @SerializedName("id")
    private Long id;
    @SerializedName("human_name")
    private String humanName;
    @SerializedName("logic_name")
    private String logicName;
    @SerializedName("value")
    private String value;
    @SerializedName("type")
    private String type;

    public Property(String humanName, String logicName, String value) {
        setHumanName(humanName);
        setLogicName(logicName);
        setValue(value);
        setType("Calculated");
    }

    public Property(String type, String logicName, String humanName, String value) {
        setHumanName(humanName);
        setLogicName(logicName);
        setValue(value);
        setType(type);
    }
}
