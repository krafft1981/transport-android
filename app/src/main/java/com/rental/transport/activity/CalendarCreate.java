package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.model.Transport;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarCreate extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void buildTimeLines(View root) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.calendar_create, container, false);
        TextView type = root.findViewById(R.id.calendarType);
        root.findViewById(R.id.calendarType).setOnClickListener(v -> {
            FragmentService
                    .getInstance()
                    .back(getActivity());
        });

        if (MemoryService
                .getInstance()
                .getProperty().get("useTransport").equals("no")) {

            type.setText("отгула");
        } else type.setText("заказа");

        root.findViewById(R.id.buttonOk).setOnClickListener(v -> {

            Transport transport = MemoryService.getInstance().getTransport();

            Long startAt = 0L;
            Long stopAt = 0L;
            Long day = 0L;

            ProgresService.getInstance().showProgress(getContext(), getString(R.string.order_creating));

            if (MemoryService
                    .getInstance()
                    .getProperty().get("useTransport").equals("no")) {

                NetworkService
                        .getInstance()
                        .getCalendarApi()
                        .doPutOutRequest(day, startAt, stopAt)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                ProgresService.getInstance().hideProgress();
                                if (response.isSuccessful()) {
                                    Toast
                                            .makeText(getContext(), "Создано", Toast.LENGTH_LONG)
                                            .show();
                                    buildTimeLines(root);
                                } else {
                                    Toast
                                            .makeText(getContext(), "Что то пошло не так", Toast.LENGTH_LONG)
                                            .show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                ProgresService.getInstance().hideProgress();
                                Toast
                                        .makeText(getContext(), t.toString(), Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

            } else {

                NetworkService
                        .getInstance()
                        .getOrderApi()
                        .doPostOrderRequest(transport.getId(), startAt, stopAt)
                        .enqueue(new Callback<Long>() {
                            @Override
                            public void onResponse(Call<Long> call, Response<Long> response) {
                                ProgresService.getInstance().hideProgress();
                                if (response.isSuccessful()) {
                                    Toast
                                            .makeText(getContext(), "Создано", Toast.LENGTH_LONG)
                                            .show();
                                    buildTimeLines(root);
                                } else {
                                    Toast
                                            .makeText(getContext(), "Что то пошло не так", Toast.LENGTH_LONG)
                                            .show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Long> call, Throwable t) {
                                ProgresService.getInstance().hideProgress();
                                Toast
                                        .makeText(getContext(), t.toString(), Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
            }
        });

        root.findViewById(R.id.buttonCancel).setOnClickListener(v -> {
            FragmentService
                    .getInstance()
                    .back(getActivity());
        });

        buildTimeLines(root);
        return root;
    }
}
