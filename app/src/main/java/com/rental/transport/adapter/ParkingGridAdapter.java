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

import java.util.ArrayList;

public class ParkingGridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Parking> data;

    public ParkingGridAdapter(Context context, ArrayList<Parking> data) {

        this.context = context;
        this.data = data;
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
    public View getView(int id, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = inflater.inflate(R.layout.parking_element, null);

        Parking parking = data.get(id);
        TextView name = item.findViewById(R.id.parking_name);
        name.setText(
                PropertyService
                        .getInstance(context)
                        .getValue(parking.getProperty(), "name")
        );

        TextView address = item.findViewById(R.id.parking_address);
        address.setText(
                PropertyService
                        .getInstance(context)
                        .getValue(parking.getProperty(), "address")
        );

        ImageView image = item.findViewById(R.id.gridview_image);

        Long maxImageId = Long.MIN_VALUE;
        for (Long imageId : parking.getImage())
            if (maxImageId < imageId)
                maxImageId = imageId;


        ImageService
                .getInstance(context)
                .setImage(maxImageId, R.drawable.unnamed, image);

        return item;
    }
}
