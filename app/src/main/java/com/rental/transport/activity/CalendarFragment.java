package com.rental.transport.activity;

import android.os.Bundle;
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
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void showDayEvents(FrameLayout frame, Date day) {

        frame.removeAllViews();

        ProgresService
                .getInstance()
                .showProgress(getActivity(), getString(R.string.calendar_loading));

        Long days[] = {day.getTime()};
        NetworkService
                .getInstance()
                .getCalendarApi()
                .doGetCustomerCalendar(days)
                .enqueue(new Callback<List<com.rental.transport.model.Calendar>>() {
                    @Override
                    public void onResponse(Call<List<com.rental.transport.model.Calendar>> call, Response<List<com.rental.transport.model.Calendar>> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful()) {
                            ListView listView = new ListView(getContext());
                            listView.setAdapter(new CalendarListAdapter(getActivity(), response.body()));
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));

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

        date.setText(dateFormatter.format(currentDate));

        root.findViewById(R.id.calendarLeft).setOnClickListener(v -> {
            Calendar calendar12 = new GregorianCalendar();
            calendar12.setTimeZone(TimeZone.getTimeZone("UTC"));
            calendar12.setTime(currentDate);
            calendar12.add(Calendar.DATE, -1);
            currentDate = calendar12.getTime();
            date.setText(dateFormatter.format(currentDate));
            showDayEvents(frame, currentDate);
        });

        root.findViewById(R.id.calendarRight).setOnClickListener(v -> {
            Calendar calendar1 = new GregorianCalendar();
            calendar1.setTimeZone(TimeZone.getTimeZone("UTC"));
            calendar1.setTime(currentDate);
            calendar1.add(Calendar.DATE, 1);
            currentDate = calendar1.getTime();
            date.setText(dateFormatter.format(currentDate));
            showDayEvents(frame, currentDate);
        });

        root.findViewById(R.id.calendarDay).setOnClickListener(v -> {
            frame.removeAllViews();
            CalendarView cv = new CalendarView(getContext());
            cv.setDate(currentDate.getTime());
            cv.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
                currentDate = new Date(year - 1900, month, dayOfMonth);
                date.setText(dateFormatter.format(currentDate));
                showDayEvents(frame, currentDate);
            });
            frame.addView(cv);
        });

        root.findViewById(R.id.floating_action_button).setOnClickListener(v -> {

            MemoryService
                    .getInstance()
                    .getProperty().put("useTransport", "no");

            FragmentService
                    .getInstance()
                    .load(getActivity(), "CalendarCreate");
        });

        showDayEvents(frame, currentDate);
        return root;
    }
}
