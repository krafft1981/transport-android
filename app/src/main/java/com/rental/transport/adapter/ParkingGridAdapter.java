package com.rental.transport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rental.transport.R;
import com.rental.transport.model.Parking;
import com.rental.transport.service.ImageService;
import com.rental.transport.service.PropertyService;

import java.util.List;

public class ParkingGridAdapter extends BaseAdapter {

    private Context context;
    private List<Parking> data;

    public ParkingGridAdapter(Context context, List<Parking> data) {

        this.context = context;
        this.data = data;
    }

    public class ViewHolder {
        ImageView image;
        TextView name;
        TextView address;
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

        ParkingGridAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.parking_element, parent, false);
            holder = new ParkingGridAdapter.ViewHolder();

            holder.image = convertView.findViewById(R.id.gridviewImage);
            holder.name = convertView.findViewById(R.id.parkingName);
            holder.address = convertView.findViewById(R.id.parkingAddress);
            convertView.setTag(holder);
        } else {
            holder = (ParkingGridAdapter.ViewHolder) convertView.getTag();
        }

        Parking parking = data.get(position);

        holder.name.setText(
                PropertyService
                        .getInstance()
                        .getValue(parking.getProperty(), "parking_name")
        );

        holder.address.setText(
                PropertyService
                        .getInstance()
                        .getValue(parking.getProperty(), "parking_address")
        );

        ImageService
                .getInstance()
                .setImage(context, parking.getImage(), position, R.drawable.transport, holder.image);

        return convertView;
    }
}
