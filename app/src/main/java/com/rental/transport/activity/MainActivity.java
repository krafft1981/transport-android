package com.rental.transport.activity;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.rental.transport.R;
import com.rental.transport.service.FragmentService;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentActivity activity = this;

        if (savedInstanceState == null) {
            FragmentService.getInstance().load(activity, "CustomerLogin");
        }
    }
}
