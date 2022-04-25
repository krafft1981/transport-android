package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import lombok.NonNull;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarFragment extends Fragment {

    private Long currentDay = new Date().getTime();

    private void deleteNoteRecord(TimeView timeView, Long noteId) {

        ProgresService.getInstance().showProgress(getContext(), getString(R.string.events_loading));
        NetworkService
                .getInstance()
                .getCalendarApi()
                .doDeleteCalendarNote(noteId)
                .enqueue(new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.code() == 401)
                            FragmentService.getInstance().load(getActivity(), "CustomerLogin");
                        else {
                            if (response.isSuccessful()) {
                                timeView.setData(response.body());
                                timeView.invalidate();
                            } else {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast
                                            .makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG)
                                            .show();
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Event>> call, Throwable t) {
                        ProgresService.getInstance().hideProgress();
                        Toast
                                .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    private void createNoteRecord(TimeView timeView, Long day, Integer[] hours, String message) {

        ProgresService.getInstance().showProgress(getContext(), getString(R.string.events_loading));
        NetworkService
                .getInstance()
                .getCalendarApi()
                .doPostCalendarNote(hours, day, new Text(message))
                .enqueue(new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.code() == 401)
                            FragmentService.getInstance().load(getActivity(), "CustomerLogin");
                        else {
                            if (response.isSuccessful()) {
                                timeView.setData(response.body());
                                timeView.invalidate();
                            } else {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast
                                            .makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG)
                                            .show();
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Event>> call, Throwable t) {
                        ProgresService.getInstance().hideProgress();
                        Toast
                                .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    private void updateNoteRecord(TimeView timeView, Long calendarId, String message) {

        ProgresService.getInstance().showProgress(getContext(), getString(R.string.events_loading));
        NetworkService
                .getInstance()
                .getCalendarApi()
                .doPutCalendarNote(calendarId, new Text(message))
                .enqueue(new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.code() == 401)
                            FragmentService.getInstance().load(getActivity(), "CustomerLogin");
                        else {
                            if (response.isSuccessful()) {
                                timeView.setData(response.body());
                                timeView.invalidate();
                            } else {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast
                                            .makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG)
                                            .show();
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Event>> call, Throwable t) {
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
                .doGetCustomerCalendar(currentDay)
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
                            } else {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast
                                            .makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG)
                                            .show();
                                } catch (Exception e) {
                                    System.out.println(e);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.calendar_fragment, container, false);
        TimeView timeView = root.findViewById(R.id.calendarContainer);
        CalendarView cv = root.findViewById(R.id.calendarBody);
        EditText note = root.findViewById(R.id.calendarNote);
        Button buttonLeft = root.findViewById(R.id.calendarActionLeft);
        Button buttonRight = root.findViewById(R.id.calendarActionRight);
        cv.setDate(currentDay);

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(cv.getDate());
        gc.setTimeZone(TimeZone.getTimeZone("GMT"));
        gc.set(java.util.Calendar.HOUR_OF_DAY, 0);
        gc.set(java.util.Calendar.MINUTE, 0);
        gc.set(java.util.Calendar.SECOND, 0);
        gc.set(java.util.Calendar.MILLISECOND, 0);
        currentDay = gc.getTimeInMillis();

        cv.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            GregorianCalendar cal = new GregorianCalendar(year, month, dayOfMonth);
            cal.setTimeZone(TimeZone.getTimeZone("GMT"));
            currentDay = cal.getTimeInMillis();
            loadDetails(timeView);
            note.setVisibility(View.GONE);
            MemoryService.getInstance().setCalendar(null);
        });

        buttonLeft.setOnClickListener(view -> {
            if (timeView.getHours().isEmpty()) {
                Calendar calendar = MemoryService.getInstance().getCalendar();
                if (calendar != null)
                    updateNoteRecord(timeView, calendar.getId(), note.getText().toString());
            } else {
                createNoteRecord(timeView, currentDay, timeView.getHours().toArray(new Integer[timeView.getHours().size()]), "");
                MemoryService.getInstance().setCalendar(null);
            }
        });

        buttonRight.setOnClickListener(view -> {
            Calendar calendar = MemoryService.getInstance().getCalendar();
            timeView.clearHours();
            note.setVisibility(View.GONE);
            if (calendar != null) {
                deleteNoteRecord(timeView, calendar.getId());
                MemoryService.getInstance().setCalendar(null);
            }
        });

        timeView.setOnTouchListener((view, event) -> {
            switch (timeView.click(view, event)) {
                case ORDER:
                case NOTE: {
                    note.setVisibility(View.VISIBLE);
                    note.setText(MemoryService.getInstance().getCalendar().getNote());
                    timeView.clearHours();
                    break;
                }

                case FREE:
                case REQUEST: {
                    note.setVisibility(View.GONE);
                    MemoryService.getInstance().setCalendar(null);
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
