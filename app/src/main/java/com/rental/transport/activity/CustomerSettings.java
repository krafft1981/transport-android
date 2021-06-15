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

    private int currentImage = 0;
    private final int PICK_IMAGE_SELECTED = 100;
    private final int STORAGE_PERMISSION_CODE = 1;
    private Gallery gallery;
    private View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Customer customer = MemoryService.getInstance().getCustomer();

        root = inflater.inflate(R.layout.customer_settings, container, false);
        ListView list = root.findViewById(R.id.property);
        list.setAdapter(new PropertyListAdapter(getContext(), customer.getProperty(), true));

        gallery = root.findViewById(R.id.gallery);
        gallery.setAdapter(new CustomerGalleryAdapter(getContext(), customer.getImage()));
        gallery.setOnItemClickListener((parent, view, position, id) -> {
            MemoryService.getInstance().setImageId(customer.getImage().get(position));
            FragmentService.getInstance().load(getActivity(), "PictureFragment");
        });

        if (customer.getImage().size() == 0)
            root.findViewById(R.id.buttonDelete).setEnabled(false);

        root.findViewById(R.id.buttonDelete).setOnClickListener(v -> {

            Long imageId = (Long) gallery.getAdapter().getItem(currentImage);

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
                                Customer customer = response.body();
                                MemoryService.getInstance().setCustomer(customer);
                                gallery.setAdapter(new CustomerGalleryAdapter(getContext(), customer.getImage()));
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

            if (customer.getImage().isEmpty())
                v.setEnabled(false);
        });

        root.findViewById(R.id.buttonSave).setOnClickListener(v -> {

            customer.setProperty(PropertyService.getInstance().getPropertyFromList(list));
            ProgresService.getInstance().showProgress(getContext(), getString(R.string.customer_saving));
            NetworkService
                    .getInstance()
                    .getCustomerApi()
                    .doPutCustomer(customer)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            ProgresService.getInstance().hideProgress();
                            if (!response.isSuccessful()) {
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
                        public void onFailure(Call<Void> call, Throwable t) {
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

                    ImageService.getInstance().getImage(getActivity(), PICK_IMAGE_SELECTED);
                }
                else {

                    ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, STORAGE_PERMISSION_CODE);
                }
            }
            else {

                ImageService.getInstance().getImage(getActivity(), PICK_IMAGE_SELECTED);
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

                        Toast
                                .makeText(getContext(), imageReturnedIntent.toString(), Toast.LENGTH_LONG)
                                .show();

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
                                            Customer customer = response.body();
                                            MemoryService.getInstance().setCustomer(customer);
                                            gallery.setAdapter(new CustomerGalleryAdapter(getContext(), customer.getImage()));
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
