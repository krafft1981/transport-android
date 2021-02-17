package com.rental.transport.activity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.adapter.CalendarListAdapter;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarFragment extends Fragment {

    private String format = "d MMMM yyyy";
    private Date currentDate = Calendar.getInstance().getTime();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void showDayEvents(FrameLayout frame, Date day) {

        frame.removeAllViews();

        ProgresService.getInstance().showProgress(getString(R.string.calendar_loading));
        Long days[] = {day.getTime()};
        NetworkService
                .getInstance()
                .getCalendarApi()
                .doGetCustomerCalendar(days)
                .enqueue(new Callback<Set<com.rental.transport.model.Calendar>>() {
                    @Override
                    public void onResponse(Call<Set<com.rental.transport.model.Calendar>> call, Response<Set<com.rental.transport.model.Calendar>> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful()) {
                            Set<com.rental.transport.model.Calendar> data = response.body();
                            CalendarListAdapter adapter = new CalendarListAdapter(getActivity(), new ArrayList(data));
                            ListView listView = new ListView(getContext());
                            listView.setAdapter(adapter);
                            frame.addView(listView);
                        }
                    }

                    @Override
                    public void onFailure(Call<Set<com.rental.transport.model.Calendar>> call, Throwable t) {
                        ProgresService.getInstance().hideProgress();
                        Toast
                                .makeText(getContext(), t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.calendar_fragment, container, false);
        FrameLayout frame = root.findViewById(R.id.calendarBody);
        Button date = root.findViewById(R.id.calendarDay);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(currentDate);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        currentDate = calendar.getTime();

        date.setText(DateFormat.format(format, currentDate));

        root.findViewById(R.id.calendarLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
                calendar.setTime(currentDate);
                calendar.add(Calendar.DATE, -1);
                currentDate = calendar.getTime();
                date.setText(DateFormat.format(format, currentDate));
                showDayEvents(frame, currentDate);
            }
        });

        root.findViewById(R.id.calendarRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
                calendar.setTime(currentDate);
                calendar.add(Calendar.DATE, 1);
                currentDate = calendar.getTime();
                date.setText(DateFormat.format(format, currentDate));
                showDayEvents(frame, currentDate);
            }
        });

        root.findViewById(R.id.calendarDay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frame.removeAllViews();
                CalendarView calendarView = new CalendarView(getContext());
                calendarView.setDate(currentDate.getTime());
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                        currentDate = new Date(year - 1900, month, dayOfMonth);
                        date.setText(DateFormat.format(format, currentDate));
                        showDayEvents(frame, currentDate);
                    }
                });
                frame.addView(calendarView);
            }
        });

        showDayEvents(frame, currentDate);
        return root;
    }
}
