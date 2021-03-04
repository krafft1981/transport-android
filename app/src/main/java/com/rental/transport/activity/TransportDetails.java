package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.adapter.PropertyListAdapter;
import com.rental.transport.adapter.TransportGalleryAdapter;
import com.rental.transport.model.Customer;
import com.rental.transport.model.Property;
import com.rental.transport.model.Transport;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;
import com.rental.transport.service.PropertyService;

import java.util.List;

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

        Button action = new Button(getContext());
        if (editable) {
            action.setText(getString(R.string.save));
            action.setOnClickListener(v -> {

                transport.setProperty(
                        PropertyService
                        .getInstance()
                        .getPropertyFromList(listView)
                );

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
            });
        }
        else {
            action.setText(getString(R.string.toOrder));
            action.setOnClickListener(v -> {
                MemoryService
                        .getInstance()
                        .getProperty().put("useTransport", "yes");

                FragmentService
                        .getInstance()
                        .load(getActivity(), "CalendarCreate");
            });
        }

        linearLayout.addView(action);

        return root;
    }
}
