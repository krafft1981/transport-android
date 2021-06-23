package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.model.Calendar;
import com.rental.transport.model.Event;
import com.rental.transport.model.Text;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;
import com.rental.transport.views.TimeView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarFragment extends Fragment {

    private Date currentDay = new Date();

    private void deleteNoteRecord(Long noteId) {

        ProgresService.getInstance().showProgress(getContext(), getString(R.string.events_loading));
        NetworkService
                .getInstance()
                .getCalendarApi()
                .doDeleteCalendarNote(noteId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful())

                            FragmentService.getInstance().load(getActivity(), "CalendarFragment");
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

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        ProgresService.getInstance().hideProgress();
                        Toast
                                .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    private void createNoteRecord(Long day, Integer[] hours, String message) {

        ProgresService.getInstance().showProgress(getContext(), getString(R.string.events_loading));
        NetworkService
                .getInstance()
                .getCalendarApi()
                .doPostCalendarNote(hours, day, new Text(message))
                .enqueue(new Callback<Calendar>() {
                    @Override
                    public void onResponse(Call<Calendar> call, Response<Calendar> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful())

                            FragmentService.getInstance().load(getActivity(), "CalendarFragment");
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

                    @Override
                    public void onFailure(Call<Calendar> call, Throwable t) {
                        ProgresService.getInstance().hideProgress();
                        Toast
                                .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    private void updateNoteRecord(Long noteId, String message) {

        ProgresService.getInstance().showProgress(getContext(), getString(R.string.events_loading));
        NetworkService
                .getInstance()
                .getCalendarApi()
                .doPutCalendarNote(noteId, new Text(message))
                .enqueue(new Callback<Calendar>() {
                    @Override
                    public void onResponse(Call<Calendar> call, Response<Calendar> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful())
                            FragmentService.getInstance().load(getActivity(), "CalendarFragment");

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

                    @Override
                    public void onFailure(Call<Calendar> call, Throwable t) {
                        ProgresService.getInstance().hideProgress();
                        Toast
                                .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }


    private void loadDetails(TimeView tv) {

        ProgresService.getInstance().showProgress(getContext(), getString(R.string.calendar_loading));
        NetworkService
                .getInstance()
                .getCalendarApi()
                .doGetCustomerCalendar(currentDay.getTime())
                .enqueue(new Callback<Map<Integer, Event>>() {
                    @Override
                    public void onResponse(Call<Map<Integer, Event>> call, Response<Map<Integer, Event>> response) {
                        ProgresService.getInstance().hideProgress();
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

                    @Override
                    public void onFailure(Call<Map<Integer, Event>> call, @NonNull Throwable t) {
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

        root.findViewById(R.id.calendarCreateRequest).setOnClickListener(view -> {
            Calendar calendar = new Calendar(currentDay.getTime(), new ArrayList(timeView.getHours()));
            MemoryService.getInstance().setCalendar(calendar);
        });

        timeView.setOnTouchListener((view, event) -> {
            switch (timeView.click(view, event)) {
                case ORDER: {

                    break;
                }

                case NOTEBOOK: {

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
