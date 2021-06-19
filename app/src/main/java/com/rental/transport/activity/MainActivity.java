package com.rental.transport.activity;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.rental.transport.R;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.NotifyService;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
            FragmentService.getInstance().load(this, "CustomerLogin");
    }
}
