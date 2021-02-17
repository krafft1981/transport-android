package com.rental.transport.service;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class SoundService {
    private static SoundService mInstance;

    private SoundService() {
    }

    public static SoundService getInstance() {

        if (mInstance == null)
            mInstance = new SoundService();

        return mInstance;
    }

    public void playNotify(Context context) {

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
