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
import com.rental.transport.model.Request;
import com.rental.transport.model.Transport;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;
import com.rental.transport.service.PropertyService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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
    private List<Request> data = new ArrayList();

    private void setData(List<Request> data) {
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
            Request p1 = (Request) o1;
            Request p2 = (Request) o2;
            return p1.getDay().compareTo(p2.getDay());
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
    public Object getItem(int id) {
        return data.get(id);
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
        }

        else
            holder = (RequestListAdapter.ViewHolder) convertView.getTag();

        Request request = data.get(position);
        Transport transport = request.getTransport();
        String name = PropertyService.getInstance().getValue(transport.getProperty(), "transport_name");

        holder.requestDay.setText(df.format(format, new Date(request.getDay())));
        holder.requestHours.setText(request.getMinHour() + ":00" + " - " + request.getMaxHour() + ":00");

        return convertView;
    }

    private void acceptRequest(Integer position) {

        Request request = data.get(position);
        ProgresService.getInstance().showProgress(context, context.getString(R.string.events_loading));
        NetworkService
                .getInstance()
                .getOrderApi()
                .doPostConfirmOrder(request.getId())
                .enqueue(new Callback<List<Request>>() {
                    @Override
                    public void onResponse(Call<List<Request>> call, Response<List<Request>> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful()) {
                            setData(response.body());
                        }
                        else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast
                                        .makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG)
                                        .show();
                            }
                            catch (Exception e) {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Request>> call, @NonNull Throwable t) {
                        ProgresService.getInstance().hideProgress();
                        Toast
                                .makeText(context, t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    private void rejectRequest(Integer position) {

        Request request = data.get(position);
        ProgresService.getInstance().showProgress(context, context.getString(R.string.events_loading));
        NetworkService
                .getInstance()
                .getOrderApi()
                .doPostRejectOrder(request.getId())
                .enqueue(new Callback<List<Request>>() {
                    @Override
                    public void onResponse(Call<List<Request>> call, Response<List<Request>> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful()) {
                            setData(response.body());
                        }
                        else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast
                                        .makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG)
                                        .show();
                            }
                            catch (Exception e) {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Request>> call, @NonNull Throwable t) {
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
                .getOrderApi()
                .doGetRequestAsDriver()
                .enqueue(new Callback<List<Request>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Request>> call, @NonNull Response<List<Request>> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful()) {
                            setData(response.body());
                        }
                        else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast
                                        .makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG)
                                        .show();
                            }
                            catch (Exception e) {
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Request>> call, @NonNull Throwable t) {
                        ProgresService.getInstance().hideProgress();
                        Toast
                                .makeText(context, t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }
}
