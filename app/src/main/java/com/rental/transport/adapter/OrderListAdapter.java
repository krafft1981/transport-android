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
import com.rental.transport.service.PropertyService;

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

public class OrderListAdapter extends BaseAdapter {

    private static final String format = "d MMMM (EEE)";
    private Context context;

    @Getter
    private List<Order> data = new ArrayList();

    public OrderListAdapter(Context context) {
        this.context = context;
        loadOrders();
    }

    private void sort() {
        Collections.sort(this.data, (Comparator) (o1, o2) -> {
            Order p1 = (Order) o1;
            Order p2 = (Order) o2;
            return p2.getDay().compareTo(p1.getDay());
        });
    }

    public class ViewHolder {
        TextView orderDay;
        TextView orderHours;
        TextView orderCustomerPhone;
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
            holder.orderDay = convertView.findViewById(R.id.orderDay);
            holder.orderHours = convertView.findViewById(R.id.orderHours);
            holder.orderCustomerPhone = convertView.findViewById(R.id.orderCustomerPhone);
            convertView.setTag(holder);
        }
        else {
            holder = (OrderListAdapter.ViewHolder) convertView.getTag();
        }

        Order order = data.get(position);

        Integer min = Integer.MAX_VALUE;
        Integer max = Integer.MIN_VALUE;

        for (Integer value : order.getHours()) {
            if (min > value) min = value;
            if (max < value) max = value;
        }

        max++;

        android.text.format.DateFormat df = new android.text.format.DateFormat();

        String phone = PropertyService.getInstance().getValue(order.getProperty(), "order_customer_phone");

        holder.orderDay.setText(df.format(format, new Date(order.getDay())));
        holder.orderHours.setText(min + ":00" + " - " + max + ":00");
        holder.orderCustomerPhone.setText(phone);
        return convertView;
    }

    private void loadOrders() {

        OrderListAdapter adapter = this;
        ProgresService.getInstance().showProgress(context, context.getString(R.string.events_loading));
        NetworkService
                .getInstance()
                .getOrderApi()
                .doGetOrderByDriver()
                .enqueue(new Callback<List<Order>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Order>> call, @NonNull Response<List<Order>> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful()) {
                            data = response.body();
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

