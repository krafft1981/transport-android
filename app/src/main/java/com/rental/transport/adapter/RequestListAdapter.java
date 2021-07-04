package com.rental.transport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rental.transport.R;
import com.rental.transport.model.Calendar;
import com.rental.transport.model.Event;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import lombok.Getter;
import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestListAdapter extends BaseAdapter {

    private static final String format = "d MMMM (EEE)";
    private android.text.format.DateFormat df = new android.text.format.DateFormat();
    private Context context;

    @Getter
    private List<Event> data = new ArrayList();

    private void setData(List<Event> data) {
        this.data = data;
        sort();
        this.notifyDataSetInvalidated();
    }

    public RequestListAdapter(Context context) {
        this.context = context;
        loadRequest();
    }

    private void sort() {
        Collections.sort(this.data, (Comparator) (o1, o2) -> {
            Event p1 = (Event) o1;
            Event p2 = (Event) o2;
            return p1.getObjectId().compareTo(p2.getObjectId());
        });
    }

    public class ViewHolder {
        TextView requestDay;
        TextView requestHours;
        TextView transportType;
        TextView transportName;
        Spinner action;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RequestListAdapter.ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.request_element, parent, false);
            holder = new RequestListAdapter.ViewHolder();
            holder.requestDay = convertView.findViewById(R.id.requestDay);
            holder.requestHours = convertView.findViewById(R.id.requestHours);
            holder.transportType = convertView.findViewById(R.id.transportType);
            holder.transportName = convertView.findViewById(R.id.transportName);
            holder.action = convertView.findViewById(R.id.action);
            holder.action.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    switch (pos) {
                        case 1: {
                            acceptRequest(position);
                            break;
                        }
                        case 2: {
                            rejectRequest(position);
                            break;
                        }
                        default:
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

            convertView.setTag(holder);
        } else
            holder = (RequestListAdapter.ViewHolder) convertView.getTag();

        Event event = data.get(position);
        Calendar calendar = event.getCalendar();

        holder.requestDay.setText(df.format(format, new Date(calendar.getDay())));

        StringBuilder builder = new StringBuilder();
        builder.append(calendar.getMinHour());
        builder.append(":00");
        builder.append(" - ");
        builder.append(calendar.getMaxHour() + 1);
        builder.append(":00");

        holder.requestHours.setText(builder.toString());

        return convertView;
    }

    private void acceptRequest(Integer position) {

        Event event = data.get(position);
        ProgresService.getInstance().showProgress(context, context.getString(R.string.events_loading));
        NetworkService
                .getInstance()
                .getRequestApi()
                .doPostConfirmRequest(event.getObjectId())
                .enqueue(new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful())
                            setData(response.body());
                        else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast
                                        .makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG)
                                        .show();
                            } catch (Exception e) {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Event>> call, @NonNull Throwable t) {
                        ProgresService.getInstance().hideProgress();
                        Toast
                                .makeText(context, t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    private void rejectRequest(Integer position) {

        Event event = data.get(position);
        ProgresService.getInstance().showProgress(context, context.getString(R.string.events_loading));
        NetworkService
                .getInstance()
                .getRequestApi()
                .doPostRejectRequest(event.getObjectId())
                .enqueue(new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful())
                            setData(response.body());
                        else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast
                                        .makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG)
                                        .show();
                            } catch (Exception e) {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Event>> call, @NonNull Throwable t) {
                        ProgresService.getInstance().hideProgress();
                        Toast
                                .makeText(context, t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    private void loadRequest() {

        ProgresService.getInstance().showProgress(context, context.getString(R.string.events_loading));
        NetworkService
                .getInstance()
                .getRequestApi()
                .doGetRequestAsDriver()
                .enqueue(new Callback<List<Event>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Event>> call, @NonNull Response<List<Event>> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful())
                            setData(response.body());
                        else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast
                                        .makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG)
                                        .show();
                            } catch (Exception e) {
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Event>> call, @NonNull Throwable t) {
                        ProgresService.getInstance().hideProgress();
                        Toast
                                .makeText(context, t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }
}
