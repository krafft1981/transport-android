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

import java.util.ArrayList;

public class TransportGridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Transport> data;

    public TransportGridAdapter(Context context, ArrayList<Transport> data) {

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
        View item = inflater.inflate(R.layout.transport_element, null);

        Transport transport = data.get(id);

        TextView type = (TextView) item.findViewById(R.id.transportType);
        type.setText(transport.getType().getName());

        TextView name = (TextView) item.findViewById(R.id.transportName);
        name.setText(
                PropertyService
                        .getInstance(context)
                        .getValue(transport.getProperty(), "name")
        );

        TextView cost = (TextView) item.findViewById(R.id.transportCost);
        cost.setText(
                PropertyService
                        .getInstance(context)
                        .getValue(transport.getProperty(), "cost")
        );

        ImageView image = item.findViewById(R.id.gridviewImage);

        Long maxImageId = Long.MIN_VALUE;
        for (Long imageId : transport.getImage())
            if (maxImageId < imageId)
                maxImageId = imageId;

        ImageService
                .getInstance(context)
                .setImage(maxImageId, R.drawable.transport, image);

        return item;
    }
}
