package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rental.transport.R;
import com.rental.transport.adapter.RequestListAdapter;

import java.util.ArrayList;

public class RequestFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.request_fragment, container, false);
        ListView list = root.findViewById(R.id.requestList);
        RequestListAdapter adapter = new RequestListAdapter(getContext(), new ArrayList());
        list.setAdapter(adapter);
        return root;
    }
}
