package com.rental.transport.activity;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.rental.transport.R;
import com.rental.transport.service.FragmentService;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle state) {

        super.onCreate(state);
        setContentView(R.layout.activity_main);

        if (state == null)
            FragmentService.getInstance().load(this, "CustomerLogin");
    }
}
