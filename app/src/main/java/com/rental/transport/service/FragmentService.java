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

    private FragmentService() {
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
    }

    public static FragmentService getInstance() {

        if (mInstance == null)
            mInstance = new FragmentService();

        return mInstance;
    }

    public void fragmentHistoryClear(AppCompatActivity activity) {

        activity.getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .commit();
    }

    public void loadFragment(AppCompatActivity activity, String name) {

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
