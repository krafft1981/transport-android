package com.rental.transport.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rental.transport.R;
import com.rental.transport.model.Calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

public class CalendarListAdapter extends BaseAdapter {

    private String format = "HH:mm:ss";

    private Context context;
    private ArrayList<Calendar> data;

    public CalendarListAdapter(Context context, ArrayList<Calendar> data) {

        this.context = context;
        this.data = data;
    }

    public class ViewHolder {
        TextView startAt;
        TextView stopAt;
        TextView order;
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
            holder.order = convertView.findViewById(R.id.calendarDetails);
            holder.startAt = convertView.findViewById(R.id.calendarStartAt);
            holder.stopAt = convertView.findViewById(R.id.calendarStopAt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Calendar calendar = data.get(position);

        holder.order.setText(calendar.getOrder() == null ? context.getString(R.string.noOrder) : calendar.getOrder().toString());
        holder.startAt.setText(DateFormat.format(format, calendar.getStartAt()));
        holder.stopAt.setText(DateFormat.format(format, calendar.getStopAt()));

//        holder.startAt.setText(calendar.getStartAt().toString());
//        holder.stopAt.setText(calendar.getStopAt().toString());

        return convertView;
    }
}
