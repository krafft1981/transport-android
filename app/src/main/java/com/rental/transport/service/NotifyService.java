package com.rental.transport.service;

import java.net.URI;
import java.net.URISyntaxException;

import tech.gusavila92.websocketclient.WebSocketClient;

public class NotifyService {

    private WebSocketClient webSocketClient = Connect();
    private static NotifyService mInstance;

    public NotifyService() {
    }

    private WebSocketClient Connect() {

        System.out.println("Connect to WebSocket");

        URI uri;
        try {
            uri = new URI("ws://138.124.187.10:8080/notify");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

        WebSocketClient client = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                send(MemoryService.getInstance().getCustomer().toString());
            }

            @Override
            public void onTextReceived(String message) {
                System.out.println("onTextReceived");
            }

            @Override
            public void onBinaryReceived(byte[] data) {
                System.out.println("onBinaryReceived");
            }

            @Override
            public void onPingReceived(byte[] data) {
                System.out.println("onPingReceived");
            }

            @Override
            public void onPongReceived(byte[] data) {
                System.out.println("onPongReceived");
            }

            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onCloseReceived() {
                System.out.println("onCloseReceived");
            }
        };

        client.setConnectTimeout(5000);
        client.setReadTimeout(60000);
        client.enableAutomaticReconnection(5000);
        client.connect();

        return client;
    }

    public static NotifyService getInstance() {

        if (mInstance == null)
            mInstance = new NotifyService();

        return mInstance;
    }
}
