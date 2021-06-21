package com.rental.transport.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.adapter.CustomerGalleryAdapter;
import com.rental.transport.adapter.PropertyListAdapter;
import com.rental.transport.model.Customer;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;
import com.rental.transport.service.PropertyService;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerSettings extends Fragment {

    private final int PICK_IMAGE_SELECTED = 100;
    private final int STORAGE_PERMISSION_CODE = 1;
    private Gallery gallery;
    private View root;
    private Button buttonDelete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Customer customer = MemoryService.getInstance().getCustomer();

        root = inflater.inflate(R.layout.customer_settings, container, false);
        buttonDelete = root.findViewById(R.id.buttonDelete);
        ListView list = root.findViewById(R.id.customerProperty);
        list.setAdapter(new PropertyListAdapter(getContext(), customer.getProperty(), true));

        gallery = root.findViewById(R.id.gallery);
        gallery.setAdapter(new CustomerGalleryAdapter(getContext()));
        gallery.setOnItemClickListener((parent, view, position, id) -> {
            Long imageId = MemoryService.getInstance().getCustomer().getImage().get(position);
            Bundle bundle = new Bundle();
            bundle.putLong("imageId", imageId);
            FragmentService.getInstance().get("PictureFragment").setArguments(bundle);
            FragmentService.getInstance().load(getActivity(), "PictureFragment");
        });

        if (customer.getImage().isEmpty())
            buttonDelete.setEnabled(false);

        buttonDelete.setOnClickListener(v -> {
            Long selected = gallery.getSelectedItemId();
            Long imageId = (Long) gallery.getAdapter().getItem(selected.intValue());
            ProgresService.getInstance().showProgress(getContext(), getString(R.string.customer_saving));
            NetworkService
                    .getInstance()
                    .getCustomerApi()
                    .doDropCustomerImage(imageId)
                    .enqueue(new Callback<Customer>() {
                        @Override
                        public void onResponse(Call<Customer> call, Response<Customer> response) {
                            ProgresService.getInstance().hideProgress();
                            if (response.isSuccessful()) {
                                MemoryService.getInstance().setCustomer(response.body());
                                gallery.setAdapter(new CustomerGalleryAdapter(getContext()));
                                gallery.invalidate();
                                if (response.body().getImage().isEmpty())
                                    buttonDelete.setEnabled(false);
                            }
                            else {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast
                                            .makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG)
                                            .show();
                                }
                                catch (Exception e) {
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Customer> call, Throwable t) {
                            ProgresService.getInstance().hideProgress();
                            Toast
                                    .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
        });

        root.findViewById(R.id.buttonSave).setOnClickListener(v -> {
            customer.setProperty(PropertyService.getInstance().getPropertyFromList(list));
            customer.setTimeZone(TimeZone.getDefault().getID());
            ProgresService.getInstance().showProgress(getContext(), getString(R.string.customer_saving));
            NetworkService
                    .getInstance()
                    .getCustomerApi()
                    .doPutCustomer(customer)
                    .enqueue(new Callback<Customer>() {
                        @Override
                        public void onResponse(Call<Customer> call, Response<Customer> response) {
                            ProgresService.getInstance().hideProgress();
                            if (response.isSuccessful()) {
                                MemoryService.getInstance().setCustomer(response.body());
                                gallery.setAdapter(new CustomerGalleryAdapter(getContext()));
                                gallery.invalidate();
                            }
                            else {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast
                                            .makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG)
                                            .show();
                                }
                                catch (Exception e) {
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Customer> call, Throwable t) {
                            ProgresService.getInstance().hideProgress();
                            Toast
                                    .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
        });

        root.findViewById(R.id.buttonLoad).setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
                if (ContextCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_GRANTED) {
                    Intent takePictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    takePictureIntent.setType("image/*");
                    if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null)
                        startActivityForResult(takePictureIntent, PICK_IMAGE_SELECTED);
                }
                else {

                    ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, STORAGE_PERMISSION_CODE);
                }
            }
            else {
                Intent takePictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                takePictureIntent.setType("image/*");
                if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null)
                    startActivityForResult(takePictureIntent, PICK_IMAGE_SELECTED);
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case PICK_IMAGE_SELECTED: {
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Bitmap image = Bitmap.createScaledBitmap(
                                MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), intent.getData()),
                                320,
                                400,
                                true
                        );
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.JPEG, 80, os);
                        byte[] data = os.toByteArray();

                        ProgresService.getInstance().showProgress(getContext(), getString(R.string.customer_saving));
                        NetworkService
                                .getInstance()
                                .getCustomerApi()
                                .doAddCustomerImage(data)
                                .enqueue(new Callback<Customer>() {
                                    @Override
                                    public void onResponse(Call<Customer> call, Response<Customer> response) {
                                        ProgresService.getInstance().hideProgress();
                                        if (response.isSuccessful()) {
                                            MemoryService.getInstance().setCustomer(response.body());
                                            gallery.setAdapter(new CustomerGalleryAdapter(getContext()));
                                            gallery.invalidate();
                                            if (!response.body().getImage().isEmpty())
                                                buttonDelete.setEnabled(true);
                                        }
                                        else {
                                            try {
                                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                                Toast
                                                        .makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG)
                                                        .show();
                                            }
                                            catch (Exception e) {
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Customer> call, Throwable t) {
                                        ProgresService.getInstance().hideProgress();
                                        Toast
                                                .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                                .show();
                                    }
                                });
                    }
                    catch (Exception e) {
                        Toast
                                .makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG)
                                .show();
                    }
                }
                break;
            }
        }
    }
}
