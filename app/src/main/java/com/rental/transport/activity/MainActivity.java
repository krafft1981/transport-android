package com.rental.transport.activity;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rental.transport.R;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;

public class MainActivity extends FragmentActivity {

    public void showMenu(Boolean show) {

        int mode = show ? View.VISIBLE : View.GONE;
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(mode);
        if (!show) {
            MemoryService.getInstance().setCustomer(null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentActivity activity = this;

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setHorizontalScrollBarEnabled(true);
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.transport_menu: {
                    FragmentService.getInstance().load(activity, "TransportDetail");
                    break;
                }

                case R.id.orders_menu: {
                    FragmentService.getInstance().load(activity, "OrdersFragment");
                    break;
                }

                case R.id.calendar_menu: {
                    FragmentService.getInstance().load(activity, "CalendarFragment");
                    break;
                }

                case R.id.account_menu: {
                    FragmentService.getInstance().load(activity, "CustomerSettings");
                    break;
                }
            }

            return true;
        });

        if (savedInstanceState == null) {
            FragmentService.getInstance().load(activity, "CustomerLogin");
        }
    }
}
