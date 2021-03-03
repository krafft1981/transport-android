package com.rental.transport.service;

import com.rental.transport.model.Customer;
import com.rental.transport.model.Parking;
import com.rental.transport.model.Transport;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class MemoryService {

    @Getter
    @Setter
    private Customer customer;

    @Getter
    @Setter
    private Transport transport;

    @Getter
    @Setter
    private Parking parking;

    @Getter
    @Setter
    private Long imageId;

    @Getter
    @Setter
    private Map property = new HashMap<String, String>();

    private static MemoryService mInstance;

    private MemoryService() {
    }

    public static MemoryService getInstance() {

        if (mInstance == null)
            mInstance = new MemoryService();

        return mInstance;
    }
}
