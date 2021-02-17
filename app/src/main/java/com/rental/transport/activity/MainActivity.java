package com.rental.transport.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rental.transport.R;
import com.rental.transport.service.FragmentService;

public class MainActivity extends AppCompatActivity {

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

        if (savedInstanceState == null) {
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.transport_menu: {
                                    FragmentService.getInstance().loadFragment(null, "TransportFragment");
                                    break;
                                }

                                case R.id.orders_menu: {
                                    FragmentService.getInstance().loadFragment(null, "OrdersFragment");
                                    break;
                                }

                                case R.id.calendar_menu: {
                                    FragmentService.getInstance().loadFragment(null, "CalendarFragment");
                                    break;
                                }

                                case R.id.parking_menu: {
                                    FragmentService.getInstance().loadFragment(null, "ParkingFragment");
                                    break;
                                }

                                case R.id.account_menu: {
                                    FragmentService.getInstance().loadFragment(null, "CustomerSettings");
                                    break;
                                }
                            }

                            return true;
                        }
                    });

            FragmentService.getInstance().loadFragment(null, "CustomerLogin");
        }
    }
}
