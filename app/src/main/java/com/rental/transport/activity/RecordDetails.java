package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.model.Calendar;
import com.rental.transport.model.Text;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordDetails extends Fragment {

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
                        if (response.isSuccessful()) {

                            FragmentService.getInstance().load(getActivity(), "CalendarFragment");
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
                    public void onFailure(Call<Void> call, Throwable t) {
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
                        if (response.isSuccessful()) {

                            FragmentService.getInstance().load(getActivity(), "CalendarFragment");
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

        root.findViewById(R.id.buttonSave).setOnClickListener(v -> {
//            updateRecord(noteId, message);
//            createRecord(day, hours, message);
        });

        root.findViewById(R.id.buttonDelete).setOnClickListener(v -> {
//            deleteRecord(noteId);
        });

        return root;
    }
}
