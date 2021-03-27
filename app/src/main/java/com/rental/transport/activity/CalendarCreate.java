package com.rental.transport.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rental.transport.R;
import com.rental.transport.model.Calendar;
import com.rental.transport.model.Customer;
import com.rental.transport.model.Event;
import com.rental.transport.model.Transport;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;
import com.rental.transport.views.FabExpander;
import com.rental.transport.views.RectangleView;

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

    private FabExpander expander_add;
    private FabExpander expander_sub;

    private boolean fabStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void drawTimeLine(FrameLayout timeShow, List<Calendar> data) {

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        timeShow.removeAllViews();
        View rectangle1 = new RectangleView(getContext(), Color.GREEN);
        rectangle1.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Clicked 1", Toast.LENGTH_SHORT).show();
        });

        rectangle1.setLayoutParams(params);
        timeShow.addView(rectangle1);

        View rectangle2 = new RectangleView(getContext(), Color.RED);
        rectangle2.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Clicked 2", Toast.LENGTH_SHORT).show();
        });

        rectangle2.setLayoutParams(params);
        rectangle2.setLeft(40);
        timeShow.addView(rectangle2);
        timeShow.invalidate();
    }

    private void refreshViews(FrameLayout timeShow) {

        Customer customer = MemoryService.getInstance().getCustomer();

        ProgresService.getInstance().showProgress(getContext(), getString(R.string.events_loading));

        if (!makeOrder) {
            NetworkService
                    .getInstance()
                    .getOrderApi()
                    .doGetCustomerCalendar(currentDate)
                    .enqueue(new Callback<List<Event>>() {
                        @Override
                        public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                            ProgresService.getInstance().hideProgress();
//                            if (response.isSuccessful())
//                            drawTimeLine(timeShow, response.body());
                        }

                        @Override
                        public void onFailure(Call<List<Event>> call, Throwable t) {
                            ProgresService.getInstance().hideProgress();
                            Toast
                                    .makeText(getContext(), t.toString(), Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

        } else {
//            Transport transport = MemoryService.getInstance().getTransport();
//            NetworkService
//                    .getInstance()
//                    .getOrderApi()
//                    .doGetCustomerTransportCalendar(transport.getId(), customer.getId(), currentDate)
//                    .enqueue(new Callback<List<Calendar>>() {
//                        @Override
//                        public void onResponse(Call<List<Calendar>> call, Response<List<Calendar>> response) {
//                            ProgresService.getInstance().hideProgress();
//                            if (response.isSuccessful())
//                            drawTimeLine(timeShow, response.body());
//                        }
//
//                        @Override
//                        public void onFailure(Call<List<Calendar>> call, Throwable t) {
//                            ProgresService.getInstance().hideProgress();
//                            Toast
//                                    .makeText(getContext(), t.toString(), Toast.LENGTH_LONG)
//                                    .show();
//                        }
//                    });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fabStatus = false;

        View root = inflater.inflate(R.layout.calendar_create, container, false);
        TextView type = root.findViewById(R.id.calendarType);
        FrameLayout calendarCreate = root.findViewById(R.id.calendarCreate);

        makeOrder = !MemoryService.getInstance().getProperty().get("useTransport").equals("no");
        if (makeOrder)
            type.setText("заказа");
        else
            type.setText("отгула");

        CalendarView calendarView = root.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            currentDate = new Date(year - 1900, month, dayOfMonth + 1).getTime();
            refreshViews(calendarCreate);
        });

        expander_add = new FabExpander(
                root.findViewById(R.id.floating_action_in_button),
                AnimationUtils.loadAnimation(getContext(), R.anim.fab_top_show),
                AnimationUtils.loadAnimation(getContext(), R.anim.fab_top_hide),
                1.7, 0.25
        );

        expander_sub = new FabExpander(
                root.findViewById(R.id.floating_action_out_button),
                AnimationUtils.loadAnimation(getContext(), R.anim.fab_bottom_show),
                AnimationUtils.loadAnimation(getContext(), R.anim.fab_bottom_hide),
                0.25, 1.7
        );

        root.findViewById(R.id.floating_action_in_button).setOnClickListener(v -> {

            if (makeOrder) {
                Transport transport = MemoryService.getInstance().getTransport();
                ProgresService.getInstance().showProgress(getContext(), getString(R.string.order_creating));
                NetworkService
                        .getInstance()
                        .getOrderApi()
                        .doPostRequest(transport.getId(), currentDate, start, stop)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                ProgresService.getInstance().hideProgress();
                                if (response.isSuccessful())
                                    refreshViews(calendarCreate);
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
//                ProgresService.getInstance().showProgress(getContext(), getString(R.string.event_creating));
//                NetworkService
//                        .getInstance()
//                        .getCalendarApi()
//                        .doPutOutRequest(currentDate, start, stop)
//                        .enqueue(new Callback<Void>() {
//                            @Override
//                            public void onResponse(Call<Void> call, Response<Void> response) {
//                                ProgresService.getInstance().hideProgress();
////                                if (response.isSuccessful())
////                                    refreshViews(timeShow);
//                            }
//
//                            @Override
//                            public void onFailure(Call<Void> call, Throwable t) {
//                                ProgresService.getInstance().hideProgress();
//                                Toast
//                                        .makeText(getContext(), t.toString(), Toast.LENGTH_LONG)
//                                        .show();
//                            }
//                        });
            }
        });

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

        root.findViewById(R.id.floating_action_out_button).setOnClickListener(v -> {

            if (makeOrder)
                FragmentService
                        .getInstance()
                        .load(getActivity(), "TransportFragment");
            else
                FragmentService
                        .getInstance()
                        .load(getActivity(), "CalendarFragment");
        });

        refreshViews(calendarCreate);
        return root;
    }
}
