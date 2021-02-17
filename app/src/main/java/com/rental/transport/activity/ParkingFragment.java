package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import java.util.Set;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingFragment extends Fragment {

    private Integer page = 0;
    private Integer size = 100;

    private void loadData(GridView grid) {

        ProgresService.getInstance().showProgress(getString(R.string.parking_loading));
        NetworkService
                .getInstance()
                .getParkingApi()
                .doGetParkingList(page, size)
                .enqueue(new Callback<Set<Parking>>() {
                    @Override
                    public void onResponse(@NonNull Call<Set<Parking>> call, @NonNull Response<Set<Parking>> response) {

                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful()) {
                            Set<Parking> parking = response.body();
                            if (parking != null) {
                                ParkingGridAdapter adapter = new ParkingGridAdapter(getActivity(), new ArrayList<>(parking));
                                grid.setAdapter(adapter);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Set<Parking>> call, @NonNull Throwable t) {
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
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_transport_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.parking_fragment, container, false);
        GridView grid = root.findViewById(R.id.parkingGridView);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                MemoryService
                        .getInstance()
                        .setParking((Parking) parent.getAdapter().getItem(position));

                FragmentService.getInstance().loadFragment("ParkingDetails");
            }
        });

        grid.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        loadData(grid);
        return root;
    }
}
