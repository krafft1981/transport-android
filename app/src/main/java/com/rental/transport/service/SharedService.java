package com.rental.transport.service;

import android.content.Context;
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

        settings = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(activity.getString(R.string.preferred_username), username);
        editor.putString(activity.getString(R.string.preferred_password), password);
        editor.commit();
    }

    public void setValue(FragmentActivity activity, String key, String value) {
        settings = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getValue(FragmentActivity activity, String key) {
        settings = activity.getPreferences(Context.MODE_PRIVATE);
        return settings.getString(key, null);
    }
}
