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
import com.rental.transport.model.Transport;

import java.net.URI;
import java.net.URISyntaxException;

import tech.gusavila92.websocketclient.WebSocketClient;

public class NotifyService {

    // Объявим переменную в начале класса
    private int counter = 1;

    // Идентификатор канала
    private static String CHANNEL_ID = "Client";

    private WebSocketClient webSocketClient = null;
    private static NotifyService mInstance;

    public NotifyService() {
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
                send(id.toString());
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
        client.send(id.toString());
        return client;
    }

    public static NotifyService getInstance() {

        if (mInstance == null)
            mInstance = new NotifyService();

        return mInstance;
    }

    public void Notify(Context context, String text) {

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
