package com.rental.transport.service;

import android.content.SharedPreferences;

import androidx.fragment.app.FragmentActivity;

import com.rental.transport.R;

public class SharedService {

    private static SharedService mInstance;
    private SharedPreferences settings;

    public SharedService() {
    }

    public static SharedService getInstance() {

        if (mInstance == null)
            mInstance = new SharedService();

        return mInstance;
    }

    public void clear() {

        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }

    public void save(FragmentActivity activity, String username, String password) {

        settings = activity.getPreferences(activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(activity.getString(R.string.preferred_username), username);
        editor.putString(activity.getString(R.string.preferred_password), password);
        editor.commit();
    }

    public String getUsername(FragmentActivity activity) {

        settings = activity.getPreferences(activity.MODE_PRIVATE);
        return settings.getString(activity.getString(R.string.preferred_username), null);
    }

    public String getPassword(FragmentActivity activity) {

        settings = activity.getPreferences(activity.MODE_PRIVATE);
        return settings.getString(activity.getString(R.string.preferred_password), null);
    }
}
