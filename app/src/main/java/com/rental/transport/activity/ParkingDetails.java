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
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.adapter.ParkingGalleryAdapter;
import com.rental.transport.adapter.PropertyListAdapter;
import com.rental.transport.adapter.TransportListAdapter;
import com.rental.transport.model.Customer;
import com.rental.transport.model.Parking;
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

public class ParkingDetails extends Fragment {

    private void loadTransport(ListView listView, Long parkingId) {

        ProgresService.getInstance().showProgress(getContext(), getString(R.string.transport_loading));
        NetworkService
                .getInstance()
                .getTransportApi()
                .doGetParkingTransport(parkingId)
                .enqueue(new Callback<List<Transport>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Transport>> call, @NonNull Response<List<Transport>> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful())
                            listView.setAdapter(new TransportListAdapter(getActivity(), response.body()));
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Transport>> call, @NonNull Throwable t) {
                        ProgresService.getInstance().hideProgress();
                        Toast
                                .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.parking_details, container, false);
        Parking parking = MemoryService.getInstance().getParking();
        Customer customer = MemoryService.getInstance().getCustomer();
        ListView listView = root.findViewById(R.id.property);
        LinearLayout buttonLayout = root.findViewById(R.id.buttonLayout);
        Boolean editable = parking.getCustomer().contains(customer.getId());

        Gallery gallery = root.findViewById(R.id.gallery);
        gallery.setAdapter(new ParkingGalleryAdapter(getContext(), parking.getImage()));
        gallery.setOnItemClickListener((parent, view, position, id) -> {
            MemoryService.getInstance().setImageId(parking.getImage().get(position));
            FragmentService
                    .getInstance()
                    .load(getActivity(), "PictureFragment");
        });

        ListView transportList = root.findViewById(R.id.transport);
        loadTransport(transportList, parking.getId());
        transportList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MemoryService.getInstance().setTransport((Transport) parent.getAdapter().getItem(position));
                FragmentService.getInstance().load(getActivity(), "TransportDetails");
            }
        });

        PropertyListAdapter adapter = new PropertyListAdapter(getContext(), parking.getProperty(), editable);
        listView.setAdapter(adapter);

        Button map = new Button(getContext());
        map.setText(getString(R.string.map));
        map.setOnClickListener(v -> FragmentService.getInstance().load(getActivity(), "MapFragment"));

        if (editable) {
            Button action = new Button(getContext());
            action.setText(getString(R.string.save));
            action.setOnClickListener(v -> {
                parking.setProperty(PropertyService.getInstance().getPropertyFromList(listView));
                ProgresService.getInstance().showProgress(getContext(), getString(R.string.parking_saving));
                NetworkService
                        .getInstance()
                        .getParkingApi()
                        .doPutParking(parking)
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
            buttonLayout.addView(action);
        }
        buttonLayout.addView(map);
        return root;
    }
}
