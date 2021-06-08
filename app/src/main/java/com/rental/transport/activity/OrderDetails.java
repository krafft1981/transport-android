package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.adapter.PropertyListAdapter;
import com.rental.transport.model.Order;
import com.rental.transport.service.MemoryService;

public class OrderDetails extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Order order = MemoryService.getInstance().getOrder();
        View root = inflater.inflate(R.layout.order_details, container, false);
        ListView list = root.findViewById(R.id.property);
        list.setAdapter(new PropertyListAdapter(getContext(), order.getProperty(), false));

        Toast
                .makeText(getActivity(), order.getProperty().toString(), Toast.LENGTH_LONG)
                .show();

        return root;
    }
}
