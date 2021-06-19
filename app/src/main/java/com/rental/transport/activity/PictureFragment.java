package com.rental.transport.activity;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.model.Transport;
import com.rental.transport.service.ImageService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.PropertyService;

public class PictureFragment extends Fragment {

    private Integer counter = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.picture_fragment, container, false);
        ImageView image = root.findViewById(R.id.image);
        Long imageId = MemoryService
                .getInstance()
                .getImageId();

        ImageService
                .getInstance()
                .setImage(getContext(), imageId, R.drawable.border, image);

        image.setOnClickListener(v -> {
            Intent notificationIntent = new Intent();
            PendingIntent pendingIntent = PendingIntent
                    .getActivity(getContext(),
                            0,
                            notificationIntent,
                            PendingIntent.FLAG_CANCEL_CURRENT
                    );

            Transport transport = MemoryService
                    .getInstance()
                    .getTransport();

            String name = PropertyService.getInstance().getValue(transport.getProperty(), "transport_name");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "Client")
                    .setSmallIcon(R.drawable.transport)
                    .setContentText("Вам одобрен заказ яхты '" + name + "'")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setLargeIcon(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.transport))
                    .setAutoCancel(false);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
            notificationManager.notify(counter++, builder.build());
        });
        return root;
    }
}
