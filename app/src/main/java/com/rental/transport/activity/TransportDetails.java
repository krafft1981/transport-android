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
import com.rental.transport.adapter.PropertyListAdapter;
import com.rental.transport.adapter.TransportGalleryAdapter;
import com.rental.transport.model.Customer;
import com.rental.transport.model.Transport;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;

public class TransportDetails extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.transport_details, container, false);

        Transport transport = MemoryService.getInstance().getTransport();
        Customer customer = MemoryService.getInstance().getCustomer();

        Gallery gallery = root.findViewById(R.id.gallery);
        gallery.setAdapter(new TransportGalleryAdapter(getContext(), transport.getImage()));
        gallery.setOnItemClickListener((parent, view, position, id) -> {
            MemoryService.getInstance().setImageId(transport.getImage().get(position));
            FragmentService
                    .getInstance()
                    .load(getActivity(), "PictureFragment");
        });

        Boolean editable = transport.getCustomer().contains(customer.getId());

        ListView listView = root.findViewById(R.id.property);
        LinearLayout linearLayout = root.findViewById(R.id.buttonLayout);

        PropertyListAdapter adapter = new PropertyListAdapter(getContext(), transport.getProperty(), editable);
        listView.setAdapter(adapter);

        if (!editable) {
            Button action = new Button(getContext());
            action.setText(getString(R.string.toOrder));
            action.setOnClickListener(v -> {
                MemoryService
                        .getInstance()
                        .getProperty().put("useTransport", "yes");

                FragmentService
                        .getInstance()
                        .load(getActivity(), "CalendarCreate");
            });
            linearLayout.addView(action);
        }

        return root;
    }
}
