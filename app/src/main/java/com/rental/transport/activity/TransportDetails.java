package com.rental.transport.activity;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.transport_details, container, false);

        Transport transport = MemoryService.getInstance().getTransport();
        Customer customer = MemoryService.getInstance().getCustomer();
        Boolean editable = transport.getCustomer().contains(customer.getId());
        Gallery gallery = root.findViewById(R.id.gallery);
        gallery.setAdapter(new TransportGalleryAdapter(getContext(), transport.getImage(), editable));
        gallery.setOnItemClickListener((parent, view, position, id) -> {
            MemoryService.getInstance().setImageId(transport.getImage().get(position));
            FragmentService.getInstance().load(getActivity(), "PictureFragment");
        });

        ListView listView = root.findViewById(R.id.property);
        listView.setAdapter(new PropertyListAdapter(getContext(), transport.getProperty(), editable));

        root.findViewById(R.id.calendarCreateRequest).setOnClickListener(view -> {
            FragmentService.getInstance().load(getActivity(), "CalendarFragment");
        });

        root.findViewById(R.id.transportMap).setOnClickListener(view -> {
            FragmentService.getInstance().load(getActivity(), "MapFragment");
        });

        return root;
    }
}
