package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rental.transport.R;
import com.rental.transport.adapter.ParkingGridAdapter;
import com.rental.transport.model.Parking;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;
import com.rental.transport.views.FabExpander;

import java.util.List;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingFragment extends Fragment {

    private Integer page = 0;
    private Integer size = 100;

    private FabExpander expander_add;
    private FabExpander expander_sub;

    private boolean fabStatus;

    private void loadParking(GridView grid, Boolean editable) {

        ProgresService.getInstance().showProgress(getContext(), getString(R.string.parking_loading));
        NetworkService
                .getInstance()
                .getParkingApi()
                .doGetParkingList(page, size)
                .enqueue(new Callback<List<Parking>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Parking>> call, @NonNull Response<List<Parking>> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful())
                            grid.setAdapter(new ParkingGridAdapter(getActivity(), response.body()));
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Parking>> call, @NonNull Throwable t) {
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

        View root = inflater.inflate(R.layout.parking_fragment, container, false);
        GridView grid = root.findViewById(R.id.parkingGridView);
        grid.setOnItemClickListener((parent, v, position, id) -> {
            MemoryService.getInstance().setParking((Parking) parent.getAdapter().getItem(position));
            FragmentService.getInstance().load(getActivity(), "ParkingDetails");
        });

        expander_add = new FabExpander(
                root.findViewById(R.id.floating_action_add_button),
                AnimationUtils.loadAnimation(getContext(), R.anim.fab_top_show),
                AnimationUtils.loadAnimation(getContext(), R.anim.fab_top_hide),
                1.7, 0.25
        );

        expander_sub = new FabExpander(
                root.findViewById(R.id.floating_action_sub_button),
                AnimationUtils.loadAnimation(getContext(), R.anim.fab_bottom_show),
                AnimationUtils.loadAnimation(getContext(), R.anim.fab_bottom_hide),
                0.25, 1.7
        );

        FloatingActionButton fab = root.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
            if (fabStatus) {
                expander_add.hide();
                expander_sub.hide();
                fabStatus = false;
            } else {
                expander_add.expand();
                expander_sub.expand();
                fabStatus = true;
            }
        });

        expander_add.fab.setOnClickListener(view -> {
            Toast
                    .makeText(getActivity(), getString(R.string.forbidden), Toast.LENGTH_LONG)
                    .show();
        });

        expander_sub.fab.setOnClickListener(view -> {
            Toast
                    .makeText(getActivity(), getString(R.string.forbidden), Toast.LENGTH_LONG)
                    .show();
        });

        grid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        loadParking(grid, false);
        return root;
    }
}
