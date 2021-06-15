package com.rental.transport.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.rental.transport.R;
import com.rental.transport.service.ImageService;
import com.rental.transport.service.MemoryService;

import java.util.List;

public class TransportGalleryAdapter extends BaseAdapter {

    private Context context;
    private List<Long> data;

    public TransportGalleryAdapter(Context context) {
        this.context = context;
        this.data = MemoryService.getInstance().getTransport().getImage();
    }

    // returns the number of images, in our example it is 10
    public int getCount() {
        return data.size();
    }

    // returns the Item  of an item, i.e. for our example we can get the image
    public Object getItem(int position) {
        return data.get(position);
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

        Integer height = Resources.getSystem().getDisplayMetrics().heightPixels;
        Integer width = Resources.getSystem().getDisplayMetrics().widthPixels;

        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // code for portrait mode
            height = height / 2;
        }
        else {
            // code for landscape mode
        }

        android.widget.Gallery.LayoutParams params = new android.widget.Gallery.LayoutParams(width, height);
        image.setLayoutParams(params);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ImageService
                .getInstance()
                .setImage(context, data, position, R.drawable.transport, image);

        return image;
    }
}
