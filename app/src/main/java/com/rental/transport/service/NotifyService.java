package com.rental.transport.service;

public class NotifyService {

    private static NotifyService mInstance;

    public NotifyService() {
    }

    private void Connect() {

    }

    public static NotifyService getInstance() {

        if (mInstance == null)
            mInstance = new NotifyService();

        return mInstance;
    }
}
