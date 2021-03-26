package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.rental.transport.views.FabExpander;

import java.util.List;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingDetails extends Fragment {

    private FabExpander expander_add;
    private FabExpander expander_sub;
    private FabExpander expander_save;

    private boolean fabStatus;

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

        fabStatus = false;

        View root = inflater.inflate(R.layout.parking_details, container, false);
        Parking parking = MemoryService.getInstance().getParking();
        Customer customer = MemoryService.getInstance().getCustomer();
        ListView listView = root.findViewById(R.id.property);
        Boolean editable = parking.getCustomer().contains(customer.getId());

        Gallery gallery = root.findViewById(R.id.gallery);
        gallery.setAdapter(new ParkingGalleryAdapter(getContext(), parking.getImage(), editable));
        gallery.setOnItemClickListener((parent, view, position, id) -> {
            MemoryService.getInstance().setImageId(parking.getImage().get(position));
            FragmentService.getInstance().load(getActivity(), "PictureFragment");
        });

        ListView transportList = root.findViewById(R.id.transport);
        loadTransport(transportList, parking.getId());
        transportList.setOnItemClickListener((parent, view, position, id) -> {
            MemoryService.getInstance().setTransport((Transport) parent.getAdapter().getItem(position));
            FragmentService.getInstance().load(getActivity(), "TransportDetails");
        });

        listView.setAdapter(new PropertyListAdapter(getContext(), parking.getProperty(), editable));
        root.findViewById(R.id.buttonMap).setOnClickListener(v -> FragmentService.getInstance().load(getActivity(), "MapFragment"));

        if (editable) {
            expander_add = new FabExpander(
                    root.findViewById(R.id.floating_action_in_button),
                    AnimationUtils.loadAnimation(getContext(), R.anim.fab_top_show),
                    AnimationUtils.loadAnimation(getContext(), R.anim.fab_top_hide),
                    1.7, 0.25
            );

            expander_sub = new FabExpander(
                    root.findViewById(R.id.floating_action_sub_button),
                    AnimationUtils.loadAnimation(getContext(), R.anim.fab_middle_show),
                    AnimationUtils.loadAnimation(getContext(), R.anim.fab_middle_hide),
                    1.5, 1.5
            );

            expander_save = new FabExpander(
                    root.findViewById(R.id.floating_action_save_button),
                    AnimationUtils.loadAnimation(getContext(), R.anim.fab_bottom_show),
                    AnimationUtils.loadAnimation(getContext(), R.anim.fab_bottom_hide),
                    0.25, 1.7
            );

            FloatingActionButton fab = root.findViewById(R.id.floatingActionButton);
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(view -> {
                if (fabStatus) {
                    expander_add.hide();
                    expander_sub.hide();
                    expander_save.hide();
                    fabStatus = false;
                } else {
                    expander_add.expand();
                    expander_sub.expand();
                    expander_save.expand();
                    fabStatus = true;
                }
            });

            expander_save.fab.setOnClickListener(view -> {

                expander_add.hide();
                expander_sub.hide();
                expander_save.hide();
                fabStatus = false;

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

            expander_add.fab.setOnClickListener(view -> {

                expander_add.hide();
                expander_sub.hide();
                expander_save.hide();
                fabStatus = false;

                Toast
                        .makeText(getActivity(), getString(R.string.forbidden), Toast.LENGTH_LONG)
                        .show();
            });

            expander_sub.fab.setOnClickListener(view -> {

                expander_add.hide();
                expander_sub.hide();
                expander_save.hide();
                fabStatus = false;

                Toast
                        .makeText(getActivity(), getString(R.string.forbidden), Toast.LENGTH_LONG)
                        .show();
            });
        }

        return root;
    }
}
