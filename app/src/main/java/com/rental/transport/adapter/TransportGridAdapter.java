package com.rental.transport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rental.transport.R;
import com.rental.transport.model.Transport;
import com.rental.transport.service.ImageService;
import com.rental.transport.service.PropertyService;

import java.util.List;

public class TransportGridAdapter extends BaseAdapter {

    private Context context;
    private List<Transport> data;

    public TransportGridAdapter(Context context, List<Transport> data) {

        this.context = context;
        this.data = data;
    }

    public class ViewHolder {
        ImageView image;
        TextView type;
        TextView name;
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

        TransportGridAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.transport_element, parent, false);
            holder = new TransportGridAdapter.ViewHolder();

            holder.image = convertView.findViewById(R.id.gridviewImage);
            holder.type = convertView.findViewById(R.id.transportType);
            holder.name = convertView.findViewById(R.id.transportName);
            convertView.setTag(holder);
        } else {
            holder = (TransportGridAdapter.ViewHolder) convertView.getTag();
        }

        Transport transport = data.get(position);

        holder.type.setText(transport.getType().getName());
        holder.name.setText(
                PropertyService
                        .getInstance()
                        .getValue(transport.getProperty(), "transport_name")
        );

        ImageService
                .getInstance()
                .setImage(context, transport.getImage(), R.drawable.transport, holder.image);

        return convertView;
    }
}
