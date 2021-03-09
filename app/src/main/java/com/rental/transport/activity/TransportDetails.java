package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rental.transport.R;
import com.rental.transport.adapter.PropertyListAdapter;
import com.rental.transport.adapter.TransportGalleryAdapter;
import com.rental.transport.model.Customer;
import com.rental.transport.model.Transport;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;
import com.rental.transport.service.PropertyService;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        Gallery gallery = root.findViewById(R.id.gallery);
        gallery.setAdapter(new TransportGalleryAdapter(getContext(), transport.getImage()));
        gallery.setOnItemClickListener((parent, view, position, id) -> {
            MemoryService.getInstance().setImageId(transport.getImage().get(position));
            FragmentService.getInstance().load(getActivity(), "PictureFragment");
        });

        Boolean editable = transport.getCustomer().contains(customer.getId());

        ListView listView = root.findViewById(R.id.property);
        listView.setAdapter(new PropertyListAdapter(getContext(), transport.getProperty(), editable));

        FloatingActionButton fab = root.findViewById(R.id.floatingActionButton);
        if (editable)
            fab.setImageResource(android.R.drawable.ic_menu_save);
        else
            fab.setImageResource(android.R.drawable.ic_menu_today);

        fab.setOnClickListener(view -> {
            if (editable) {
                transport.setProperty(PropertyService.getInstance().getPropertyFromList(listView));
                ProgresService.getInstance().showProgress(getContext(), getString(R.string.transport_saving));
                NetworkService
                        .getInstance()
                        .getTransportApi()
                        .doPutTransport(transport)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                                ProgresService.getInstance().hideProgress();
                            }

                            @Override
                            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                ProgresService.getInstance().hideProgress();
                                Toast
                                        .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
            } else {
                MemoryService.getInstance().getProperty().put("useTransport", "yes");
                FragmentService.getInstance().load(getActivity(), "CalendarCreate");
            }
        });

        return root;
    }
}
