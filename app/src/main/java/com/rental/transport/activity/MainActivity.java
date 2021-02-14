package com.rental.transport.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rental.transport.R;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    Fragment CustomerLogin = new CustomerLogin();
    Fragment TransportFragment = new TransportFragment();
    Fragment MapFragment = new MapFragment();
    Fragment CustomerCreate = new CustomerCreate();
    Fragment CalendarFragment = new CalendarFragment();
    Fragment ParkingDetails = new ParkingDetails();
    Fragment TransportDetails = new TransportDetails();

    public void fragmentHistoryClear() {

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .commit();
    }

    public void loadFragment(String name) {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(name);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out
                )
//                .replace(R.id.main_activity_container, CustomerLogin, name)
                .commit();
    }

    public void showProgress(String message) {

        if (progressDialog == null) {
            try {
                progressDialog = ProgressDialog.show(this, "", message);
                progressDialog.setCancelable(false);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void hideProgress() {

        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

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
                                    loadFragment("TransportFragment");
                                    break;
                                }

                                case R.id.orders_menu: {
                                    loadFragment("OrdersFragment");
                                    break;
                                }

                                case R.id.calendar_menu: {
                                    loadFragment("CalendarFragment");
                                    break;
                                }

                                case R.id.parking_menu: {
                                    loadFragment("ParkingFragment");
                                    break;
                                }

                                case R.id.account_menu: {
                                    loadFragment("CustomerSettings");
                                    break;
                                }
                            }

                            return true;
                        }
                    });

            loadFragment("CustomerLogin");
        }
    }
}
