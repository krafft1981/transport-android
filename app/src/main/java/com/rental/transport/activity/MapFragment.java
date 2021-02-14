package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;

public class MapFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            String msg = bundle.getString("name");
//            if (msg != null) {
//            }
//        }

        View root = inflater.inflate(R.layout.map_fragment, container, false);
        return root;
    }
}
