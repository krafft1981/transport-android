package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rental.transport.R;
import com.rental.transport.adapter.CalendarListAdapter;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;
import com.rental.transport.views.FabExpander;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarFragment extends Fragment {

    private Date currentDate = Calendar.getInstance().getTime();
    SimpleDateFormat dateFormatter = new SimpleDateFormat("d MMMM yyyy");

    private FabExpander expander_add;
    private FabExpander expander_sub;

    private boolean fabStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void showCalendar(FrameLayout frame, Button date) {
        frame.removeAllViews();
        CalendarView cv = new CalendarView(getContext());
        cv.setDateTextAppearance(android.R.style.TextAppearance_Medium);
        cv.setWeekDayTextAppearance(android.R.style.TextAppearance_Medium);
        cv.setDate(currentDate.getTime());
        cv.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            currentDate = new Date(year - 1900, month, dayOfMonth + 1);
            date.setText(dateFormatter.format(currentDate));
            showDayEvents(frame, currentDate, false);
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.addRule(RelativeLayout.ABOVE, R.id.calendarBody);
        cv.setLayoutParams(params);
        frame.addView(cv);
    }

    private void showDayEvents(FrameLayout frame, Date day, Boolean editable) {

        frame.removeAllViews();

        ProgresService.getInstance().showProgress(getActivity(), getString(R.string.calendar_loading));
        NetworkService
                .getInstance()
                .getCalendarApi()
                .doGetCustomerCalendar(day.getTime())
                .enqueue(new Callback<List<com.rental.transport.model.Calendar>>() {
                    @Override
                    public void onResponse(Call<List<com.rental.transport.model.Calendar>> call, Response<List<com.rental.transport.model.Calendar>> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful()) {
                            ListView listView = new ListView(getContext());
//                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                                    ViewGroup.LayoutParams.MATCH_PARENT,
//                                    ViewGroup.LayoutParams.MATCH_PARENT
//                            );
//                            params.addRule(RelativeLayout.ABOVE, R.id.calendarBody);
//                            listView.setLayoutParams(params);
                            listView.setAdapter(new CalendarListAdapter(getActivity(), response.body()));
                            if (editable) {
                                listView.setOnItemClickListener((parent, view, position, id) -> {
                                    Toast
                                            .makeText(getContext(), "selected item: " + String.valueOf(id), Toast.LENGTH_LONG)
                                            .show();
                                });
                            }
                            frame.addView(listView);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<com.rental.transport.model.Calendar>> call, Throwable t) {
                        ProgresService.getInstance().hideProgress();
                        Toast
                                .makeText(getContext(), t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    private Calendar getCalendar() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.setTime(currentDate);
        return calendar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fabStatus = false;

        dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        View root = inflater.inflate(R.layout.calendar_fragment, container, false);
        FrameLayout frame = root.findViewById(R.id.calendarBody);
        Button date = root.findViewById(R.id.calendarDay);

        expander_add = new FabExpander(
                root.findViewById(R.id.floating_action_add_button),
                AnimationUtils.loadAnimation(getContext(), R.anim.fab_top_show),
                AnimationUtils.loadAnimation(getContext(), R.anim.fab_top_hide),
                1.7, 0.25
        );

        expander_sub = new FabExpander(
                root.findViewById(R.id.floating_action_exit_button),
                AnimationUtils.loadAnimation(getContext(), R.anim.fab_bottom_show),
                AnimationUtils.loadAnimation(getContext(), R.anim.fab_bottom_hide),
                0.25, 1.7
        );

        {
            Calendar c = getCalendar();
            c.set(java.util.Calendar.HOUR_OF_DAY, 0);
            c.set(java.util.Calendar.MINUTE, 0);
            c.set(java.util.Calendar.SECOND, 0);
            c.set(java.util.Calendar.MILLISECOND, 0);
            currentDate = c.getTime();
        }

        date.setText(dateFormatter.format(currentDate));
        root.findViewById(R.id.calendarDay).setOnClickListener(v -> {
            showCalendar(frame, date);
        });

        root.findViewById(R.id.calendarLeft).setOnClickListener(v -> {
            Calendar c = getCalendar();
            c.add(Calendar.DATE, -1);
            currentDate = c.getTime();
            date.setText(dateFormatter.format(currentDate));
            showDayEvents(frame, currentDate, false);
        });

        root.findViewById(R.id.calendarRight).setOnClickListener(v -> {
            Calendar c = getCalendar();
            c.add(Calendar.DATE, 1);
            currentDate = c.getTime();
            date.setText(dateFormatter.format(currentDate));
            showDayEvents(frame, currentDate, false);
        });

        FloatingActionButton fab = root.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
            if (fabStatus) {
                expander_add.hide();
                expander_sub.hide();
                showDayEvents(frame, currentDate, false);
                fabStatus = false;
            } else {
                expander_add.expand();
                expander_sub.expand();
                fabStatus = true;
            }
        });

        expander_add.fab.setOnClickListener(view -> {
            MemoryService.getInstance().getProperty().put("useTransport", "no");
            FragmentService.getInstance().load(getActivity(), "CalendarCreate");
        });

        expander_sub.fab.setOnClickListener(view -> {
            showDayEvents(frame, currentDate, true);
        });

        showDayEvents(frame, currentDate, false);
        return root;
    }
}
