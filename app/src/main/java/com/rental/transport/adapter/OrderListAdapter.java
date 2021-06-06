package com.rental.transport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.rental.transport.R;
import com.rental.transport.model.Order;
import com.rental.transport.model.Request;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import lombok.Getter;
import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderListAdapter extends BaseAdapter {

    private Context context;

    @Getter
    private List<Order> data = new ArrayList();

    public OrderListAdapter(Context context) {
        this.context = context;
        loadOrders();
    }

    private void sort() {
        Collections.sort(this.data, (Comparator) (o1, o2) -> {
            Request p1 = (Request) o1;
            Request p2 = (Request) o2;
            return p2.getDay().compareTo(p1.getDay());
        });
    }

    public class ViewHolder {
        TextView orderDay;
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

        OrderListAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.order_element, parent, false);
            holder = new OrderListAdapter.ViewHolder();

//            holder.orderDay = convertView.findViewById(R.id.orderDay);


            convertView.setTag(holder);
        }
        else {
            holder = (OrderListAdapter.ViewHolder) convertView.getTag();
        }

        Order order = data.get(position);

//        holder.orderDay.setText(calendar.getTime().toString());
        return convertView;
    }

    private void loadOrders() {

        OrderListAdapter adapter = this;
        ProgresService.getInstance().showProgress(context, context.getString(R.string.events_loading));
        NetworkService
                .getInstance()
                .getOrderApi()
                .doGetOrders()
                .enqueue(new Callback<List<Order>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Order>> call, @NonNull Response<List<Order>> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful()) {
//                            data = response.body();
                            sort();
                            adapter.notifyDataSetInvalidated();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Order>> call, @NonNull Throwable t) {
                        ProgresService.getInstance().hideProgress();
                        Toast
                                .makeText(context, t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }
}

