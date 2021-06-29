package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.enums.EventTypeEnum;
import com.rental.transport.model.Event;
import com.rental.transport.model.Transport;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;
import com.rental.transport.views.TimeView;

import org.json.JSONObject;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarFragment extends Fragment {

    private Long currentDay = new Date().getTime();
    private TimeView timeView;

    private void loadDetails(TimeView tv) {

        Transport transport = MemoryService.getInstance().getTransport();

        ProgresService.getInstance().showProgress(getContext(), getString(R.string.calendar_loading));
        NetworkService
                .getInstance()
                .getCalendarApi()
                .doGetTransportCalendar(transport.getId(), currentDay)
                .enqueue(new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.code() == 401)
                            FragmentService.getInstance().load(getActivity(), "CustomerLogin");
                        else {
                            if (response.isSuccessful()) {
                                tv.setData(response.body());
                                tv.invalidate();
                            }
                            else {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast
                                            .makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG)
                                            .show();
                                }
                                catch (Exception e) {
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Event>> call, @NonNull Throwable t) {
                        ProgresService.getInstance().hideProgress();
                        Toast
                                .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }


    private void updateDetails(TimeView tv) {

        Set<Integer> hours = tv.getHours();
        Transport transport = MemoryService.getInstance().getTransport();

        ProgresService.getInstance().showProgress(getContext(), getString(R.string.events_loading));
        NetworkService
                .getInstance()
                .getRequestApi()
                .doPostRequest(transport.getId(), currentDay, hours.toArray(new Integer[hours.size()]))
                .enqueue(new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.code() == 401)
                            FragmentService.getInstance().load(getActivity(), "CustomerLogin");
                        else {
                            if (response.isSuccessful()) {
                                tv.setData(response.body());
                                tv.invalidate();
                            }
                            else {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast
                                            .makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG)
                                            .show();
                                }
                                catch (Exception e) {
                                }
                                loadDetails(tv);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Event>> call, @NonNull Throwable t) {
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
        super.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.calendar_fragment, container, false);
        timeView = root.findViewById(R.id.calendarContainer);
        CalendarView cv = root.findViewById(R.id.calendarBody);

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(cv.getDate());
        gc.setTimeZone(TimeZone.getTimeZone("GMT"));
        gc.set(java.util.Calendar.HOUR_OF_DAY, 0);
        gc.set(java.util.Calendar.MINUTE, 0);
        gc.set(java.util.Calendar.SECOND, 0);
        gc.set(java.util.Calendar.MILLISECOND, 0);
        currentDay = gc.getTimeInMillis();

        cv.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            GregorianCalendar calendar = new GregorianCalendar(year, month, dayOfMonth);
            calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
            currentDay = calendar.getTimeInMillis();
            loadDetails(timeView);
        });

        root.findViewById(R.id.calendarCreateRequest).setOnClickListener(view -> updateDetails(timeView));

        timeView.setOnTouchListener((view, event) -> {
            if (timeView.click(view, event) == EventTypeEnum.ORDER)
                FragmentService.getInstance().load(getActivity(), "OrderFragment");
            return true;
        });

        loadDetails(timeView);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
