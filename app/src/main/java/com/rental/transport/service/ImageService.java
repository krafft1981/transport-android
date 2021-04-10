package com.rental.transport.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import com.rental.transport.model.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageService {

    private static ImageService mInstance;

    private ImageService() {
    }

    public static ImageService getInstance() {

        if (mInstance == null)
            mInstance = new ImageService();

        return mInstance;
    }

    private File getFile(Context context, Long imageId) {
        return new File(context.getCacheDir(), imageId.toString());
    }

    private Boolean setImageFromCache(Context context, Long imageId, ImageView image) {

        File file = getFile(context, imageId);
        if (!file.exists())
            return false;

        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        image.setImageBitmap(bitmap);
        return true;
    }

    private void setImageAndCache(Context context, Long imageId, String data, ImageView image) {

        byte[] decodedString = Base64.decode(data, Base64.DEFAULT);
        try {
            File file = getFile(context, imageId);
            FileOutputStream out = new FileOutputStream(file);
            out.write(decodedString, 0, decodedString.length);
        }
        catch (Exception e) {

        }

        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        image.setImageBitmap(bitmap);
    }

    public void setImage(Context context, Long imageId, int defaultImage, ImageView image) {

        if (imageId == Long.MIN_VALUE) {
            image.setImageResource(defaultImage);
            image.invalidate();
            return;
        }

        if (!setImageFromCache(context, imageId, image)) {
            NetworkService
                    .getInstance()
                    .getImageApi()
                    .doGetImage(imageId)
                    .enqueue(new Callback<Image>() {
                        @Override
                        public void onResponse(Call<Image> call, Response<Image> response) {
                            if (response.isSuccessful()) {
                                String base64String = response.body().getData();
                                setImageAndCache(context, imageId, base64String, image);
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

    public void setImage(Context context, List<Long> images, int defaultImage, ImageView image) {

        Long imageId = Long.MIN_VALUE;
        for (Long id : images) {
            if (imageId < id)
                imageId = id;
        }

        setImage(context, imageId, defaultImage, image);
    }

    public void setImage(Context context, List<Long> images, Integer index, int defaultImage, ImageView image) {

        if (images.size() > index) {
            Long imageId = images.get(index);
            setImage(context, imageId, defaultImage, image);
        }
        else {
            image.setImageResource(defaultImage);
            image.invalidate();
        }
    }
}
