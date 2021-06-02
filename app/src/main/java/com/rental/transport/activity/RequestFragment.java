package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.adapter.RequestListAdapter;
import com.rental.transport.model.Event;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;

import java.util.Map;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestFragment extends Fragment {

    private void loadRequest(ListView list) {

        ProgresService.getInstance().showProgress(getContext(), getString(R.string.events_loading));
        NetworkService
                .getInstance()
                .getOrderApi()
                .doGetRequestEventByDriver()
                .enqueue(new Callback<Map<Integer, Event>>() {
                    @Override
                    public void onResponse(@NonNull Call<Map<Integer, Event>> call, @NonNull Response<Map<Integer, Event>> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful())
                            list.setAdapter(new RequestListAdapter(getContext(), response.body()));
                    }

                    @Override
                    public void onFailure(@NonNull Call<Map<Integer, Event>> call, @NonNull Throwable t) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.request_fragment, container, false);
        ListView list = root.findViewById(R.id.requestList);

        list.setOnItemClickListener((parent, view, position, id) -> {
            Object item = list.getItemAtPosition(position);
            Toast
                    .makeText(getActivity(), String.valueOf(position), Toast.LENGTH_LONG)
                    .show();
        });
        loadRequest(list);
        return root;
    }
}
