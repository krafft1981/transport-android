package com.rental.transport.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.rental.transport.R;
import com.rental.transport.service.ImageService;

import java.util.List;

public class TransportGalleryAdapter extends BaseAdapter {

    private Context context;
    private List<Long> data;
    private Boolean editable;

    public TransportGalleryAdapter(Context context, List<Long> data, Boolean editable) {
        this.context = context;
        this.data = data;
        this.editable = editable;
    }

    // returns the number of images, in our example it is 10
    public int getCount() {
        return data.size();
    }

    // returns the Item  of an item, i.e. for our example we can get the image
    public Object getItem(int position) {
        return position;
    }

    // returns the ID of an item
    public long getItemId(int position) {
        return position;
    }

    // returns an ImageView view
    public View getView(int position, View convertView, ViewGroup parent) {
        // position argument will indicate the location of image
        // create a ImageView programmatically
        ImageView image = new ImageView(context);
        ImageService
                .getInstance()
                .setImage(context, data, position, R.drawable.transport, image);

        image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        image.setAdjustViewBounds(true);
        image.setImageResource(R.drawable.icon);

        return image;
    }
}
