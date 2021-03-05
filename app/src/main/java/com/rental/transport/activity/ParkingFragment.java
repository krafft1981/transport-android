package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.adapter.ParkingGridAdapter;
import com.rental.transport.model.Parking;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingFragment extends Fragment {

    private Integer page = 0;
    private Integer size = 100;

    private void loadParking(GridView grid) {

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

        View root = inflater.inflate(R.layout.parking_fragment, container, false);
        GridView grid = root.findViewById(R.id.parkingGridView);
        grid.setOnItemClickListener((parent, v, position, id) -> {
            MemoryService.getInstance().setParking((Parking) parent.getAdapter().getItem(position));
            FragmentService.getInstance().load(getActivity(), "ParkingDetails");
        });

        grid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        loadParking(grid);
        return root;
    }
}
