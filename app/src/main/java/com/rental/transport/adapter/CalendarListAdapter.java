package com.rental.transport.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rental.transport.R;
import com.rental.transport.model.Calendar;
import com.rental.transport.model.Customer;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.PropertyService;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;

public class CalendarListAdapter extends BaseAdapter {

    private Context context;
    private List<Calendar> data;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm");

    public CalendarListAdapter(Context context, List<Calendar> data) {

        dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        this.context = context;
        this.data = data;

        Collections.sort(this.data, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Calendar p1 = (Calendar) o1;
                Calendar p2 = (Calendar) o2;
                return p1.getStartAt().compareTo(p2.getStartAt());
            }
        });
    }

    public class ViewHolder {
        TextView number;
        TextView fio;
        TextView phone;
        TextView startAt;
        TextView stopAt;
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

        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.calendar_element, parent, false);
            holder = new ViewHolder();

            holder.number = convertView.findViewById(R.id.eventId);
            holder.fio = convertView.findViewById(R.id.calendarFio);
            holder.phone = convertView.findViewById(R.id.calendarPhone);
            holder.startAt = convertView.findViewById(R.id.calendarStartAt);
            holder.stopAt = convertView.findViewById(R.id.calendarStopAt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Calendar calendar = data.get(position);

        Customer customer = MemoryService
                .getInstance()
                .getCustomer();

        String fio = PropertyService
                .getInstance()
                .getValue(customer.getProperty(), "fio");

        String phone = PropertyService
                .getInstance()
                .getValue(customer.getProperty(), "phone");

        Long orderId = calendar.getOrder();
        if (orderId != null) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.gray));

        }

        holder.number.setText(String.valueOf(position));
        holder.fio.setText(fio);

        holder.phone.setText(phone);
        holder.startAt.setText(dateFormatter.format(calendar.getStartAt()));
        holder.stopAt.setText(dateFormatter.format(calendar.getStopAt()));

        return convertView;
    }
}
