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
import com.rental.transport.adapter.TransportGridAdapter;
import com.rental.transport.model.Transport;
import com.rental.transport.service.NetworkService;

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

        ((MainActivity) getActivity()).showProgress("Загрузка транспорта");

        NetworkService
                .getInstance()
                .getTransportApi()
                .doGetTransportList(page, size)
                .enqueue(new Callback<Set<Transport>>() {
                    @Override
                    public void onResponse(@NonNull Call<Set<Transport>> call, @NonNull Response<Set<Transport>> response) {

                        ((MainActivity) getActivity()).hideProgress();

                        if (response.isSuccessful()) {
                            Set<Transport> transport = response.body();
                            TransportGridAdapter adapter = new TransportGridAdapter(getActivity(), new ArrayList<>(transport));
                            grid.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Set<Transport>> call, @NonNull Throwable t) {

                        ((MainActivity) getActivity()).hideProgress();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            String msg = bundle.getString("name");
//            if (msg != null) {
//            }
//        }

        View root = inflater.inflate(R.layout.transport_fragment, container, false);
        GridView grid = (GridView) root.findViewById(R.id.transport_gridview);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Transport transport = (Transport) parent.getAdapter().getItem(position);
                ((MainActivity) getActivity()).setTransport(transport);
                ((MainActivity) getActivity()).loadFragment("TransportDetails");
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
