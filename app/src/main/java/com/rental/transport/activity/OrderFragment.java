package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.adapter.PropertyListAdapter;
import com.rental.transport.model.Order;
import com.rental.transport.service.MemoryService;

import java.util.Date;

public class OrderFragment extends Fragment {

    private static final String format = "d MMMM (EEE)";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.order_fragment, container, false);
        Order order = MemoryService.getInstance().getOrder();
        ListView listView = root.findViewById(R.id.property);
        listView.setAdapter(new PropertyListAdapter(getContext(), order.getProperty(), false));

        Integer min = Integer.MAX_VALUE;
        Integer max = Integer.MIN_VALUE;

        for (Integer value : order.getHours()) {
            if (min > value) min = value;
            if (max < value) max = value;
        }
        max ++;

        android.text.format.DateFormat df = new android.text.format.DateFormat();

        TextView day = root.findViewById(R.id.orderDay);
        TextView hours = root.findViewById(R.id.orderHours);
        day.setText(df.format(format, new Date(order.getDay())));
        hours.setText(min + ":00" + " - " + max + ":00");

        return root;
    }
}
