package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.adapter.TransportGridAdapter;
import com.rental.transport.model.Transport;
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

public class TransportFragment extends Fragment {

    private Integer page = 0;
    private Integer size = 100;

    private void loadData(GridView grid) {

        ProgresService
                .getInstance()
                .showProgress(getContext(), getString(R.string.transport_loading));

        NetworkService
                .getInstance()
                .getTransportApi()
                .doGetTransportList(page, size)
                .enqueue(new Callback<Set<Transport>>() {
                    @Override
                    public void onResponse(@NonNull Call<Set<Transport>> call, @NonNull Response<Set<Transport>> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful()) {
                            Set<Transport> transport = response.body();
                            TransportGridAdapter adapter = new TransportGridAdapter(getActivity(), new ArrayList<>(transport));
                            grid.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Set<Transport>> call, @NonNull Throwable t) {
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

        View root = inflater.inflate(R.layout.transport_fragment, container, false);
        GridView grid = root.findViewById(R.id.transport_gridview);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                MemoryService.getInstance().setTransport(
                        (Transport) parent
                                .getAdapter()
                                .getItem(position)
                );

                FragmentService
                        .getInstance()
                        .loadFragment(getActivity(), "TransportDetails");
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
