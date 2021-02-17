package com.rental.transport.service;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgresService {
    private static ProgresService mInstance;
    private ProgressDialog progressDialog;

    public ProgresService() {}

    public static ProgresService getInstance() {

        if (mInstance == null)
            mInstance = new ProgresService();

        return mInstance;
    }

    public void showProgress(Context context, String message) {

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
