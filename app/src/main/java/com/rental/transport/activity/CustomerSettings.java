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
import com.rental.transport.adapter.CustomerGalleryAdapter;
import com.rental.transport.adapter.ParkingGalleryAdapter;
import com.rental.transport.adapter.PropertyListAdapter;
import com.rental.transport.model.Customer;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;
import com.rental.transport.service.PropertyService;
import com.rental.transport.service.SharedService;
import com.rental.transport.views.FabExpander;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerSettings extends Fragment {

    private FabExpander expander_save;
    private FabExpander expander_exit;

    private boolean fabStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fabStatus = false;

        Customer customer = MemoryService.getInstance().getCustomer();

        View root = inflater.inflate(R.layout.customer_settings, container, false);
        ListView listView = root.findViewById(R.id.property);
        PropertyListAdapter adapter = new PropertyListAdapter(getContext(), customer.getProperty(), true);
        listView.setAdapter(adapter);

        Gallery gallery = root.findViewById(R.id.gallery);
        gallery.setAdapter(new CustomerGalleryAdapter(getContext(), customer.getImage(), true));
        gallery.setOnItemClickListener((parent, view, position, id) -> {
            MemoryService.getInstance().setImageId(customer.getImage().get(position));
            FragmentService.getInstance().load(getActivity(), "PictureFragment");
        });

        expander_save = new FabExpander(
                root.findViewById(R.id.floating_action_save_button),
                AnimationUtils.loadAnimation(getContext(), R.anim.fab_top_show),
                AnimationUtils.loadAnimation(getContext(), R.anim.fab_top_hide),
                1.7, 0.25
        );

        expander_exit = new FabExpander(
                root.findViewById(R.id.floating_action_exit_button),
                AnimationUtils.loadAnimation(getContext(), R.anim.fab_bottom_show),
                AnimationUtils.loadAnimation(getContext(), R.anim.fab_bottom_hide),
                0.25, 1.7
        );

        FloatingActionButton fab = root.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
            if (fabStatus) {
                expander_save.hide();
                expander_exit.hide();
                fabStatus = false;
            } else {
                expander_save.expand();
                expander_exit.expand();
                fabStatus = true;
            }
        });

        expander_save.fab.setOnClickListener(view -> {
            customer.setProperty(PropertyService.getInstance().getPropertyFromList(listView));
            ProgresService.getInstance().showProgress(getContext(), getString(R.string.customer_saving));
            NetworkService
                    .getInstance()
                    .getCustomerApi()
                    .doPutUpdateCustomerRequest(customer)
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

        expander_exit.fab.setOnClickListener(view -> {
            SharedService.getInstance().clear();
            FragmentService.getInstance().fragmentHistoryClear(getActivity());
            FragmentService.getInstance().load(getActivity(), "CustomerLogin");
        });

        return root;
    }
}
