package com.rental.transport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rental.transport.R;
import com.rental.transport.model.Request;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import lombok.Getter;

public class OrderListAdapter extends BaseAdapter {

    private Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
    private Context context;

    @Getter
    private List<Request> data;

    public OrderListAdapter(Context context, List<Request> data) {
        this.context = context;
        this.data = data;

        Collections.sort(this.data, (Comparator) (o1, o2) -> {
            Request p1 = (Request) o1;
            Request p2 = (Request) o2;
            return p2.getDay().compareTo(p1.getDay());
        });

        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public class ViewHolder {
        TextView orderDay;
        TextView hourStart;
        TextView hourStop;
        TextView transportType;
        TextView transportName;
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
            convertView = inflater.inflate(R.layout.request_element, parent, false);
            holder = new OrderListAdapter.ViewHolder();

//            holder.orderDay = convertView.findViewById(R.id.orderDay);
//            holder.hourStart = convertView.findViewById(R.id.hourStart);
//            holder.hourStop = convertView.findViewById(R.id.hourStop);
            holder.transportType = convertView.findViewById(R.id.transportType);
            holder.transportName = convertView.findViewById(R.id.transportName);

            convertView.setTag(holder);
        }
        else {
            holder = (OrderListAdapter.ViewHolder) convertView.getTag();
        }

        Request request = data.get(position);

        calendar.setTimeInMillis(request.getDay());

        Integer min = Integer.MAX_VALUE;
        Integer max = Integer.MIN_VALUE;
        for (Integer value : request.getHours()) {

            if (min > value) min = value;
            if (max < value) max = value;
        }

        holder.orderDay.setText(calendar.getTime().toString());
//        holder.transportType.setText(request.getTransport().getType().getName());

        holder.hourStart.setText(min + ":00");
        holder.hourStop.setText(max + 1 + ":00");

        return convertView;
    }
}
