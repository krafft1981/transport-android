package com.rental.transport.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rental.transport.R;
import com.rental.transport.model.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageService {

    private Context context;
    private static ImageService mInstance;

    private ImageService(Context context) {
        this.context = context;
    }

    public static ImageService getInstance(Context context) {

        if (mInstance == null)
            mInstance = new ImageService(context);

        return mInstance;
    }

    public static ImageService getInstance() {

        return mInstance;
    }

    private File getFile(Long id) {
        return new File(context.getCacheDir(), id.toString());
    }

    private void setImageProperty(ImageView image) {

        image.setAdjustViewBounds(true);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    private Boolean setImageFromCache(Long id, ImageView image) {

        File file = getFile(id);
        if (!file.exists())
            return false;

        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        image.setImageBitmap(bitmap);
        setImageProperty(image);
        return true;
    }

    private void setImageAndCache(Long id, String data, ImageView image) {

        byte[] decodedString = Base64.decode(data, Base64.DEFAULT);
        try {
            File file = getFile(id);
            FileOutputStream out = new FileOutputStream(file);
            out.write(decodedString, 0, decodedString.length);
        } catch (Exception e) {

        }

        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        image.setImageBitmap(bitmap);
        setImageProperty(image);
    }

    private ImageView setImageFromResource(Integer resource) {

        ImageView image = new ImageView(context);
        image.setImageResource(resource);
        setImageProperty(image);
        return image;
    }

    private void setImage(Long id, int defaultImage, LinearLayout layout) {

        ImageView image = new ImageView(context);

        if (setImageFromCache(id, image)) {
            image.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            layout.addView(image);
            layout.invalidate();
        } else {
            NetworkService
                    .getInstance()
                    .getImageApi()
                    .doGetImage(id)
                    .enqueue(new Callback<Image>() {
                        @Override
                        public void onResponse(Call<Image> call, Response<Image> response) {
                            if (response.isSuccessful()) {
                                String base64String = response.body().getData();

                                setImageAndCache(id, base64String, image);
                                image.setLayoutParams(new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT
                                ));
                                layout.addView(image);
                                layout.invalidate();
                            }
                        }

                        @Override
                        public void onFailure(Call<Image> call, Throwable t) {

                            ImageView image = setImageFromResource(defaultImage);
                            image.setLayoutParams(new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                            ));
                            layout.addView(image);
                        }
                    });
        }
    }


    public ImageView setImage(Set<Long> ids, int defaultImage, LinearLayout layout, Boolean editable) {

        for (Long id : ids)
            setImage(id, defaultImage, layout);

        if (editable) {
            ImageView image = setImageFromResource(R.drawable.add);
            image.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));

            layout.addView(image);
            return image;
        }

        return null;
    }

    public void setImage(Long id, int defaultImage, ImageView image) {

        if (id == Long.MIN_VALUE) {
            image.setImageResource(defaultImage);
            image.invalidate();
            return;
        }

        if (setImageFromCache(id, image)) {

            image.invalidate();
        }
        else {
            NetworkService
                    .getInstance()
                    .getImageApi()
                    .doGetImage(id)
                    .enqueue(new Callback<Image>() {
                        @Override
                        public void onResponse(Call<Image> call, Response<Image> response) {
                            if (response.isSuccessful()) {
                                String base64String = response.body().getData();
                                setImageAndCache(id, base64String, image);
                                image.invalidate();
                            }
                        }

                        @Override
                        public void onFailure(Call<Image> call, Throwable t) {
                            image.setImageResource(defaultImage);
                            image.invalidate();
                        }
                    });
        }
    }
}
