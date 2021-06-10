package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.model.Event;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;
import com.rental.transport.views.TimeView;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarFragment extends Fragment {

    private Date currentDay = new Date();

    private void loadDetails(TimeView tv) {

        ProgresService.getInstance().showProgress(getContext(), getString(R.string.calendar_loading));
        NetworkService
                .getInstance()
                .getOrderApi()
                .doGetCustomerCalendar(currentDay.getTime())
                .enqueue(new Callback<Map<Integer, Event>>() {
                    @Override
                    public void onResponse(Call<Map<Integer, Event>> call, Response<Map<Integer, Event>> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful()) {
                            tv.setData(response.body());
                            tv.invalidate();
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<Integer, Event>> call, @NonNull Throwable t) {
                        ProgresService.getInstance().hideProgress();
                        Toast
                                .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    private void createRecord(TimeView tv) {

        Set<Integer> hours = tv.getHours();

        ProgresService.getInstance().showProgress(getContext(), getString(R.string.events_loading));
        NetworkService
                .getInstance()
                .getOrderApi()
                .doPostAbsentCustomer(currentDay.getTime(), hours.toArray(new Integer[hours.size()]))
                .enqueue(new Callback<Event>() {
                    @Override
                    public void onResponse(Call<Event> call, Response<Event> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful()) {
                            MemoryService.getInstance().setEvent(response.body());
                            FragmentService.getInstance().load(getActivity(), "RecordDetails");
                        }
                    }

                    @Override
                    public void onFailure(Call<Event> call, @NonNull Throwable t) {
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

        View root = inflater.inflate(R.layout.calendar_fragment, container, false);
        TimeView timeView = root.findViewById(R.id.calendarContainer);
        CalendarView cv = root.findViewById(R.id.calendarBody);
        cv.setDate(currentDay.getTime());

        cv.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            currentDay = new Date(year - 1900, month, dayOfMonth + 1);
            loadDetails(timeView);
        });

        root.findViewById(R.id.calendarCreateRequest).setOnClickListener(view -> createRecord(timeView));

        timeView.setOnTouchListener((view, event) -> {
            switch (timeView.click(view, event)) {
                case ORDER: {
                    FragmentService.getInstance().load(getActivity(), "OrderDetails");
                    break;
                }

                case NOTEBOOK: {
                    FragmentService.getInstance().load(getActivity(), "RecordDetails");
                    break;
                }

                default:
                    break;
            }
            return true;
        });

        loadDetails(timeView);
        return root;
    }
}
