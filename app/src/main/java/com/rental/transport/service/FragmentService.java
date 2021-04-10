package com.rental.transport.service;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.rental.transport.R;
import com.rental.transport.activity.CalendarFragment;
import com.rental.transport.activity.CustomerCreate;
import com.rental.transport.activity.CustomerLogin;
import com.rental.transport.activity.MapFragment;
import com.rental.transport.activity.PictureFragment;
import com.rental.transport.activity.TransportDetails;
import com.rental.transport.activity.TransportFragment;

import java.util.HashMap;

public class FragmentService {

    private static FragmentService mInstance;
    private HashMap<String, Fragment> frags = new HashMap<>();

    private FragmentService() {
        frags.put("CustomerLogin", new CustomerLogin());
        frags.put("TransportFragment", new TransportFragment());
        frags.put("MapFragment", new MapFragment());
        frags.put("CustomerCreate", new CustomerCreate());
        frags.put("CalendarFragment", new CalendarFragment());
        frags.put("TransportDetails", new TransportDetails());
        frags.put("PictureFragment", new PictureFragment());
    }

    public static FragmentService getInstance() {

        if (mInstance == null)
            mInstance = new FragmentService();

        return mInstance;
    }


    public void load(FragmentActivity activity, String name) {

        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_container, frags.get(name), name)
                .addToBackStack(name)
                .commit();
    }

    public Fragment get(String name) {

        return frags.get(name);
    }
}
