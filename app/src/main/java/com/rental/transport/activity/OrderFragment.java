package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.adapter.OrderListAdapter;

public class OrderFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.order_fragment, container, false);
        ListView list = root.findViewById(R.id.orderList);
        list.setAdapter(new OrderListAdapter(getContext()));
         return root;
    }
}
