package com.rental.transport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rental.transport.R;

import java.util.ArrayList;

public class CalendarGridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ViewHolder> data;

    public CalendarGridAdapter(Context context, ArrayList<ViewHolder> data) {

        this.context = context;
        this.data = data;
    }

    public class ViewHolder{
        private Integer leftId;
        private ImageView leftImage;
        private Integer day;
        private Integer rightId;
        private ImageView rightImage;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int id) {
        System.out.println("get by id: " + String.valueOf(id));
        return data.get(id);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int id, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = inflater.inflate(R.layout.calendar_element, null);

        TextView day = item.findViewById(R.id.calendarDay);
        day.setText(data.get(id).toString());

        return item;
    }
}
