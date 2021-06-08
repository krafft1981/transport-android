package com.rental.transport.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rental.transport.R;
import com.rental.transport.model.Customer;
import com.rental.transport.model.Message;
import com.rental.transport.service.MemoryService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.Getter;

public class OrderChatAdapter extends BaseAdapter {

    private static final String format = "yyyy-MM-d HH:mm:ss";
    private android.text.format.DateFormat df = new android.text.format.DateFormat();
    private Context context;

    @Getter
    private List<Message> data;

    public OrderChatAdapter(Context context, List<Message> data) {
        this.context = context;
        this.data = data;

        Collections.sort(data, (Comparator) (o1, o2) -> {
            Message p1 = (Message) o1;
            Message p2 = (Message) o2;
            return p1.getId().compareTo(p2.getId());
        });
    }

    public class ViewHolder {
        TextView date;
        TextView text;
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

        OrderChatAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.order_message, parent, false);
            holder = new OrderChatAdapter.ViewHolder();
            holder.date = convertView.findViewById(R.id.messageDate);
            holder.text = convertView.findViewById(R.id.messageText);
            convertView.setTag(holder);
        } else {
            holder = (OrderChatAdapter.ViewHolder) convertView.getTag();
        }

        Message message = data.get(position);

        Customer customer = MemoryService.getInstance().getCustomer();
        Integer gravity = customer.getId() == message.getCustomerId() ? Gravity.LEFT : Gravity.RIGHT;

        holder.date.setText(df.format(format, message.getDate()));
        holder.text.setText(message.getText());
        holder.text.setGravity(gravity);

        return convertView;
    }
}
