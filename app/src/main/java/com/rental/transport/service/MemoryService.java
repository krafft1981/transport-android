package com.rental.transport.service;

import com.rental.transport.model.Customer;
import com.rental.transport.model.Event;
import com.rental.transport.model.Order;
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
    private Order order;

    @Getter
    @Setter
    private Long imageId;

    @Getter
    @Setter
    private Event event;

    @Getter
    @Setter
    private Map<String, String> property = new HashMap<String, String>();

    private static MemoryService mInstance;

    private MemoryService() {
    }

    public static MemoryService getInstance() {

        if (mInstance == null)
            mInstance = new MemoryService();

        return mInstance;
    }
}
