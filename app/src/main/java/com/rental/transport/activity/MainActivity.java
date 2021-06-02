package com.rental.transport.activity;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rental.transport.R;
import com.rental.transport.service.FragmentService;

public class MainActivity extends FragmentActivity {

    public AHBottomNavigation bottomNavigation = null;

    public void showMenu(Boolean show) {

        int mode = show ? View.VISIBLE : View.GONE;
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(mode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        https://github.com/aurelhubert/ahbottomnavigation
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationPositionListener((AHBottomNavigation.OnNavigationPositionListener) (position, wasSelected) -> {
            // Do something cool here...
//            return true;
        });
//            switch (item.getItemId()) {
//                case R.id.transport_menu: {
//                    FragmentService.getInstance().load(this, "TransportFragment");
//                    break;
//                }
//
//                case R.id.orders_menu: {
//                    FragmentService.getInstance().load(this, "OrderFragment");
//                    break;
//                }
//
//                case R.id.calendar_menu: {
//                    FragmentService.getInstance().load(this, "CalendarFragment");
//                    break;
//                }
//
//                case R.id.account_menu: {
//                    FragmentService.getInstance().load(this, "CustomerSettings");
//                    break;
//                }
//
//                case R.id.request_menu: {
//                    FragmentService.getInstance().load(this, "RequestFragment");
//                    break;
//                }
//            }

//            return true;

        if (savedInstanceState == null)
            FragmentService.getInstance().load(this, "CustomerLogin");
    }
}
