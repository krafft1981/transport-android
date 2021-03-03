package com.rental.transport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rental.transport.R;
import com.rental.transport.model.Property;
import com.rental.transport.model.Transport;
import com.rental.transport.service.ImageService;

import java.util.List;

public class TransportListAdapter extends BaseAdapter {

    private Context context;
    private List<Transport> data;

    public TransportListAdapter(Context context, List<Transport> data) {

        this.context = context;
        this.data = data;
    }

    public class ViewHolder {
        ImageView image;
        TextView type;
        TextView name;
        TextView cost;
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

        TransportListAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.transport_element, parent, false);
            holder = new TransportListAdapter.ViewHolder();

            holder.image = convertView.findViewById(R.id.gridviewImage);
            holder.type = convertView.findViewById(R.id.transportType);
            holder.name = convertView.findViewById(R.id.transportName);
            holder.cost = convertView.findViewById(R.id.transportCost);
            convertView.setTag(holder);
        } else {
            holder = (TransportListAdapter.ViewHolder) convertView.getTag();
        }

        Transport transport = data.get(position);

        ImageService
                .getInstance()
                .setImage(context, transport.getImage(), position, R.drawable.transport, holder.image);

        holder.type.setText(transport.getType().getName());
        holder.name.setText("Название");
        holder.cost.setText("1800");

        return convertView;
    }
}
