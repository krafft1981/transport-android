package com.rental.transport.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.model.Calendar;
import com.rental.transport.model.Customer;
import com.rental.transport.model.Transport;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;
import com.rental.transport.views.FreeTime;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarCreate extends Fragment {

    private Long currentDate = new Date().getTime();
    private Long start = 0L;
    private Long stop = 0L;
    private Boolean makeOrder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void buildTimeLines(FrameLayout timeShow) {

        Customer customer = MemoryService.getInstance().getCustomer();

        ProgresService.getInstance().showProgress(getContext(), getString(R.string.events_loading));

        if (MemoryService
                .getInstance()
                .getProperty().get("useTransport").equals("no")) {

            NetworkService
                    .getInstance()
                    .getCalendarApi()
                    .doGetCustomerCalendar(currentDate)
                    .enqueue(new Callback<List<Calendar>>() {
                        @Override
                        public void onResponse(Call<List<Calendar>> call, Response<List<Calendar>> response) {
                            ProgresService.getInstance().hideProgress();
//                            if (response.isSuccessful()) {
                                timeShow.removeAllViews();
                                timeShow.addView(new FreeTime(getContext(), response.body(), Color.RED, Color.GREEN));
                                timeShow.invalidate();
//                            }
                        }

                        @Override
                        public void onFailure(Call<List<Calendar>> call, Throwable t) {
                            ProgresService.getInstance().hideProgress();
                            Toast
                                    .makeText(getContext(), t.toString(), Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

        } else {
            Transport transport = MemoryService.getInstance().getTransport();
            NetworkService
                    .getInstance()
                    .getCalendarApi()
                    .doGetCustomerTransportCalendar(transport.getId(), customer.getId(), currentDate)
                    .enqueue(new Callback<List<Calendar>>() {
                        @Override
                        public void onResponse(Call<List<Calendar>> call, Response<List<Calendar>> response) {
                            ProgresService.getInstance().hideProgress();
//                            if (response.isSuccessful()) {
                                timeShow.removeAllViews();
                                timeShow.addView(new FreeTime(getContext(), response.body(), Color.RED, Color.GREEN));
                                timeShow.invalidate();
//                            }
                        }

                        @Override
                        public void onFailure(Call<List<Calendar>> call, Throwable t) {
                            ProgresService.getInstance().hideProgress();
                            Toast
                                    .makeText(getContext(), t.toString(), Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.calendar_create, container, false);
        TextView type = root.findViewById(R.id.calendarType);
        FrameLayout timeShow = root.findViewById(R.id.timeShow);

        makeOrder = !MemoryService.getInstance().getProperty().get("useTransport").equals("no");
        if (makeOrder)
            type.setText("заказа");
        else
            type.setText("отгула");

        CalendarView calendarView = root.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            currentDate = new Date(year - 1900, month, dayOfMonth + 1).getTime();
            buildTimeLines(timeShow);
        });

        root.findViewById(R.id.buttonOk).setOnClickListener(v -> {

            if (makeOrder) {
                Transport transport = MemoryService.getInstance().getTransport();
                ProgresService.getInstance().showProgress(getContext(), getString(R.string.order_creating));
                NetworkService
                        .getInstance()
                        .getOrderApi()
                        .doPostOrderRequest(transport.getId(), currentDate, start, stop)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                ProgresService.getInstance().hideProgress();
//                                if (response.isSuccessful()) {
                                    buildTimeLines(timeShow);
//                                }
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
                ProgresService.getInstance().showProgress(getContext(), getString(R.string.event_creating));
                NetworkService
                        .getInstance()
                        .getCalendarApi()
                        .doPutOutRequest(currentDate, start, stop)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                ProgresService.getInstance().hideProgress();
//                                if (response.isSuccessful()) {
                                    buildTimeLines(timeShow);
//                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                ProgresService.getInstance().hideProgress();
                                Toast
                                        .makeText(getContext(), t.toString(), Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
            }
        });

        root.findViewById(R.id.buttonCancel).setOnClickListener(v -> {

            if (makeOrder)
                FragmentService
                        .getInstance()
                        .load(getActivity(), "TransportFragment");
            else
                FragmentService
                        .getInstance()
                        .load(getActivity(), "CalendarFragment");
        });

        buildTimeLines(timeShow);
        return root;
    }
}
