package com.rental.transport.service;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgresService {
    private Context context;
    private static ProgresService mInstance;
    private ProgressDialog progressDialog;

    public ProgresService(Context context) {
        this.context = context;
    }

    public static ProgresService getInstance(Context context) {

        if (mInstance == null)
            mInstance = new ProgresService(context);

        return mInstance;
    }

    public static ProgresService getInstance() {

        return mInstance;
    }

    public void showProgress(String message) {

        if (progressDialog == null) {
            try {
                progressDialog = ProgressDialog.show(context, "", message);
                progressDialog.setCancelable(false);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void hideProgress() {

        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
