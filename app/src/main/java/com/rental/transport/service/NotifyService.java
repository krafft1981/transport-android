package com.rental.transport.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.rental.transport.R;
import com.rental.transport.model.Customer;

import java.net.URI;
import java.net.URISyntaxException;

import tech.gusavila92.websocketclient.WebSocketClient;

public class NotifyService {

    // Объявим переменную в начале класса
    private int counter = 1;

    private Context context;

    // Идентификатор канала
    private static String CHANNEL_ID = "Client";

    private WebSocketClient webSocketClient = null;
    private static NotifyService mInstance;

    public NotifyService(Context context) {
        this.context = context;
    }

    public WebSocketClient Connect(Long id) {

        if (webSocketClient != null)
            webSocketClient.close();

        URI uri;
        try {
            uri = new URI("ws://138.124.187.10:8080/websocket");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

        WebSocketClient client = new WebSocketClient(uri) {
            @Override
            public void onOpen() {

            }

            @Override
            public void onTextReceived(String message) {
                sendNotify(context, message);
            }

            @Override
            public void onBinaryReceived(byte[] data) {
            }

            @Override
            public void onPingReceived(byte[] data) {
            }

            @Override
            public void onPongReceived(byte[] data) {
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

        Customer customer = MemoryService.getInstance().getCustomer();
        client.addHeader("username", customer.getAccount());

        client.setConnectTimeout(60000);
        client.setReadTimeout(10000);
        client.enableAutomaticReconnection(5000);
        client.connect();

        return client;
    }

    public static NotifyService getInstance(Context context) {

        if (mInstance == null)
            mInstance = new NotifyService(context);

        return mInstance;
    }

    public void sendNotify(Context context, String text) {

        Intent notificationIntent = new Intent();
        PendingIntent contentIntent = PendingIntent
                .getActivity(context,
                        0,
                        notificationIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.del)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.transport))
                .setAutoCancel(false);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(counter++, builder.build());
    }
}
