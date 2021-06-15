package com.rental.transport.service;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
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

    public byte[] getImage(Context context, Intent intent) {

        String name = getRealPathFromURI(context, intent.getData());
        Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(name), 320, 400, true);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, os);
        return os.toByteArray();
    }

    private String getRealPathFromURI(Context context, Uri uri) { // не на всех телефонах работает

        return getRealPathFromURI_API19(context, uri);
    }

    public static String getRealPathFromURI_API19(Context context, Uri uri) {

        String fileName = "";
        if (uri.getHost().contains("com.android.providers.media")) {
            // Image pick from recent
            String wholeID = DocumentsContract.getDocumentId(uri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                fileName = cursor.getString(columnIndex);
            }
            cursor.close();
            return fileName;
        }
        else {
            // image pick from gallery
            return getRealPathFromURI_BelowAPI11(context, uri);
        }
    }

    public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
