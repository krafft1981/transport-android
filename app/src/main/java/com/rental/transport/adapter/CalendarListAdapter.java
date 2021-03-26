package com.rental.transport.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.rental.transport.R;
import com.rental.transport.model.Calendar;
import com.rental.transport.model.Customer;
import com.rental.transport.model.Event;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.PropertyService;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;

public class CalendarListAdapter extends BaseAdapter {

    private Context context;
    private List<Event> data;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm");

    public CalendarListAdapter(Context context, List<Event> data) {

        dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        this.context = context;
        this.data = data;
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

        Event event = data.get(position);

        Customer customer = MemoryService
                .getInstance()
                .getCustomer();

        String fio = PropertyService
                .getInstance()
                .getValue(customer.getProperty(), "customer_fio");

        String phone = PropertyService
                .getInstance()
                .getValue(customer.getProperty(), "customer_phone");

        switch (event.getType().intValue()) {
            case 1: {
                holder.fio.setText(fio);
                holder.phone.setText(phone);
                convertView.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                break;
            }
            case 2:
                convertView.setBackgroundColor(context.getResources().getColor(R.color.red));
                break;
            case 3:
                convertView.setBackgroundColor(context.getResources().getColor(R.color.silver));
                break;
            case 4:
                convertView.setBackgroundColor(context.getResources().getColor(R.color.blue));
                break;
        }

        holder.number.setText(String.valueOf(position));
        holder.startAt.setText(dateFormatter.format(event.getCalendar().getStartAt()));
        holder.stopAt.setText(dateFormatter.format(event.getCalendar().getStopAt()));

        return convertView;
    }
}
