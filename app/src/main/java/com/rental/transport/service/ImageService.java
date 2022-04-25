package com.rental.transport.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
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

    private void setImageAndCache(Context context, Long imageId, byte[] data, ImageView image) {

        try {
            File file = getFile(context, imageId);
            FileOutputStream out = new FileOutputStream(file);
            out.write(data, 0, data.length);
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            image.setImageBitmap(bitmap);
        }
        catch (Exception e) {
            System.out.println(e);
        }
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
                    .enqueue(new Callback<byte[]>() {
                        @Override
                        public void onResponse(Call<byte[]> call, Response<byte[]> response) {
                            if (response.isSuccessful()) {
                                setImageAndCache(context, imageId, response.body(), image);
                                image.invalidate();
                            }
                        }

                        @Override
                        public void onFailure(Call<byte[]> call, Throwable t) {
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
