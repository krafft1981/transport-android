package com.rental.transport.service;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class SoundService {
    private Context context;
    private static SoundService mInstance;

    private SoundService(Context context) {
        this.context = context;
    }

    public static SoundService getInstance(Context context) {

        if (mInstance == null)
            mInstance = new SoundService(context);

        return mInstance;
    }

    public static SoundService getInstance() {

        return mInstance;
    }

    public void playNotify() {

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
