package com.rental.transport.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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
import com.rental.transport.service.ImageService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;
import com.rental.transport.service.PropertyService;

import org.json.JSONObject;

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
        ListView list = root.findViewById(R.id.property);
        list.setAdapter(new PropertyListAdapter(getContext(), customer.getProperty(), true));

        gallery = root.findViewById(R.id.gallery);
        gallery.setAdapter(new CustomerGalleryAdapter(getContext()));
        gallery.setOnItemClickListener((parent, view, position, id) -> {
            Long imageId = MemoryService.getInstance().getCustomer().getImage().get(position);
            MemoryService.getInstance().setImageId(imageId);
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
                    Intent ringIntent = new Intent();
                    ringIntent.setType("image/*");
                    ringIntent.setAction(Intent.ACTION_GET_CONTENT);
                    ringIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    this.startActivityForResult(Intent.createChooser(ringIntent, "Select Image"), PICK_IMAGE_SELECTED);
                }
                else {

                    ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, STORAGE_PERMISSION_CODE);
                }
            }
            else {
                Intent ringIntent = new Intent();
                ringIntent.setType("image/*");
                ringIntent.setAction(Intent.ACTION_GET_CONTENT);
                ringIntent.addCategory(Intent.CATEGORY_OPENABLE);
                this.startActivityForResult(Intent.createChooser(ringIntent, "Select Image"), PICK_IMAGE_SELECTED);
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case PICK_IMAGE_SELECTED: {
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        byte[] data = ImageService.getInstance().getImage(getContext(), imageReturnedIntent);
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
                    }
                }
                break;
            }
        }
    }
}
