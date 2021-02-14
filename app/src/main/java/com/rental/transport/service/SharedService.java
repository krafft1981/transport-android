package com.rental.transport.service;

import android.app.Activity;
import android.content.SharedPreferences;

import com.rental.transport.R;

public class SharedService {

    private static SharedService mInstance;
    private Activity activity;
    private SharedPreferences settings;

    public SharedService(Activity activity) {
        this.activity = activity;
        settings = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public static SharedService getInstance(Activity activity) {
        if (mInstance == null) {
            mInstance = new SharedService(activity);
        }

        return mInstance;
    }

    public void clear() {

        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }

    public void save(String username, String password) {

        SharedPreferences.Editor editor = settings.edit();
        editor.putString(activity.getString(R.string.preferred_username), username);
        editor.putString(activity.getString(R.string.preferred_password), password);
        editor.commit();
    }

    public String getUsername() {

        return settings.getString(activity.getString(R.string.preferred_username), null);
    }

    public String getPassword() {

        return settings.getString(activity.getString(R.string.preferred_password), null);
    }
}
