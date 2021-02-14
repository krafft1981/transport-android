package com.rental.transport.service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.activity.CalendarFragment;
import com.rental.transport.activity.CustomerCreate;
import com.rental.transport.activity.CustomerLogin;
import com.rental.transport.activity.CustomerSettings;
import com.rental.transport.activity.MapFragment;
import com.rental.transport.activity.OrdersFragment;
import com.rental.transport.activity.ParkingDetails;
import com.rental.transport.activity.ParkingFragment;
import com.rental.transport.activity.TransportDetails;
import com.rental.transport.activity.TransportFragment;

import java.util.HashMap;

public class FragmentService {

    private static FragmentService mInstance;
    private HashMap<String, Fragment> frags = new HashMap<>();
    private AppCompatActivity activity;

    private FragmentService(AppCompatActivity activity) {
        this.activity = activity;
        frags.put("CustomerLogin", new CustomerLogin());
        frags.put("CustomerSettings", new CustomerSettings());
        frags.put("TransportFragment", new TransportFragment());
        frags.put("OrdersFragment", new OrdersFragment());
        frags.put("ParkingFragment", new ParkingFragment());
        frags.put("MapFragment", new MapFragment());
        frags.put("CustomerCreate", new CustomerCreate());
        frags.put("CalendarFragment", new CalendarFragment());
        frags.put("ParkingDetails", new ParkingDetails());
        frags.put("TransportDetails", new TransportDetails());

        fragmentHistoryClear();
    }

    public static FragmentService getInstance(AppCompatActivity activity) {

        if (mInstance == null)
            mInstance = new FragmentService(activity);

        return mInstance;
    }

    public static FragmentService getInstance() {

        return mInstance;
    }

    public void fragmentHistoryClear() {

        activity.getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .commit();
    }

    public void loadFragment(String name) {

        activity.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out
                )
                .replace(R.id.main_activity_container, frags.get(name), name)
                .addToBackStack(name)
                .commit();
    }
}
