package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.adapter.ParkingGalleryAdapter;
import com.rental.transport.adapter.PropertyListAdapter;
import com.rental.transport.model.Customer;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.SharedService;

public class CustomerSettings extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Customer customer = MemoryService.getInstance().getCustomer();

        View root = inflater.inflate(R.layout.customer_settings, container, false);
        ListView listView = root.findViewById(R.id.property);
        PropertyListAdapter adapter = new PropertyListAdapter(getContext(), customer.getProperty(), true);
        listView.setAdapter(adapter);

        Gallery gallery = root.findViewById(R.id.gallery);
        gallery.setAdapter(new ParkingGalleryAdapter(getContext(), customer.getImage()));
        gallery.setOnItemClickListener((parent, view, position, id) -> {
            MemoryService.getInstance().setImageId(customer.getImage().get(position));
            FragmentService
                    .getInstance()
                    .load(getActivity(), "PictureFragment");
        });

        LinearLayout buttonLayout = root.findViewById(R.id.buttonLayout);
        Button exit = new Button(getContext());
        exit.setText(getString(R.string.exit));
        exit.setOnClickListener(v -> {
            SharedService.getInstance().clear();
            FragmentService.getInstance().fragmentHistoryClear(getActivity());
            FragmentService.getInstance().load(getActivity(), "CustomerLogin");
        });

        buttonLayout.addView(exit);
        return root;
    }
}
