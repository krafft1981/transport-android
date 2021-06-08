package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.adapter.OrderListAdapter;
import com.rental.transport.model.Order;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;

public class OrderFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.order_fragment, container, false);
        ListView list = root.findViewById(R.id.orderList);
        BaseAdapter adapter = new OrderListAdapter(getContext());
        list.setAdapter(adapter);
        list.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            Order order = (Order)adapter.getItem(position);
            MemoryService.getInstance().setOrder(order);
            FragmentService.getInstance().load(getActivity(), "OrderDetails");
        });

         return root;
    }
}
