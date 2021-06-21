package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.model.Calendar;
import com.rental.transport.model.Text;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;

import org.json.JSONObject;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordDetails extends Fragment {

    private static final String format = "d MMMM (EEE)";
    private android.text.format.DateFormat df = new android.text.format.DateFormat();

    private void deleteRecord(Long noteId) {

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

    private void createRecord(Long day, Integer[] hours, String message) {

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

    private void updateRecord(Long noteId, String message) {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.record_details, container, false);
        TextView dayText = root.findViewById(R.id.recordDay);
        TextView hoursText = root.findViewById(R.id.recordHours);
        EditText editText = root.findViewById(R.id.recordMessage);

        Calendar calendar = MemoryService.getInstance().getCalendar();
        dayText.setText(df.format(format, new Date(calendar.getDay())));
        hoursText.setText(calendar.getHours().toString());
        editText.setText(calendar.getNote());

        root.findViewById(R.id.buttonSave).setOnClickListener(v -> {

            Calendar c = MemoryService.getInstance().getCalendar();
            c.setNote(editText.getText().toString());
            if (c.getId() == null) {
                Integer[] hours = c.getHours().toArray(new Integer[c.getHours().size()]);
                createRecord(c.getDay(), hours, c.getNote());
            }
            else
                updateRecord(c.getId(), c.getNote());

            editText.setText("");
        });

        root.findViewById(R.id.buttonDelete).setOnClickListener(v -> {
            Calendar c = MemoryService.getInstance().getCalendar();
            if (c.getId() != null)
                editText.setText("");
            deleteRecord(c.getId());
        });

        return root;
    }
}
