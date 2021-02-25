package com.rental.transport.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rental.transport.R;
import com.rental.transport.service.FragmentService;

public class MainActivity extends FragmentActivity {

    public void showMenu(Boolean show) {

        int mode = show ? View.VISIBLE : View.GONE;
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(mode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FragmentActivity activity = this;

        if (savedInstanceState == null) {
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.transport_menu: {
                                    FragmentService.getInstance().loadFragment(activity, "TransportFragment");
                                    break;
                                }

                                case R.id.orders_menu: {
                                    FragmentService.getInstance().loadFragment(activity, "OrdersFragment");
                                    break;
                                }

                                case R.id.calendar_menu: {
                                    FragmentService.getInstance().loadFragment(activity, "CalendarFragment");
                                    break;
                                }

                                case R.id.parking_menu: {
                                    FragmentService.getInstance().loadFragment(activity, "ParkingFragment");
                                    break;
                                }

                                case R.id.account_menu: {
                                    FragmentService.getInstance().loadFragment(activity, "CustomerSettings");
                                    break;
                                }
                            }

                            return true;
                        }
                    });

            FragmentService.getInstance().loadFragment(activity, "CustomerLogin");
        }
    }
}
