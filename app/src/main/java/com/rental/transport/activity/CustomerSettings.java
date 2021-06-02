package com.rental.transport.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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
        ListView listView = root.findViewById(R.id.property);
        PropertyListAdapter adapter = new PropertyListAdapter(getContext(), customer.getProperty(), true);
        listView.setAdapter(adapter);

        gallery = root.findViewById(R.id.gallery);
        gallery.setAdapter(new CustomerGalleryAdapter(getContext(), customer.getImage()));
        gallery.setOnItemClickListener((parent, view, position, id) -> {
            MemoryService.getInstance().setImageId(customer.getImage().get(position));
            FragmentService.getInstance().load(getActivity(), "PictureFragment");
        });

        if (customer.getImage().size() == 0)
            root.findViewById(R.id.buttonDelete).setEnabled(false);

        root.findViewById(R.id.buttonDelete).setOnClickListener(v -> {

            customer.getImage().remove(customer.getImage().get(currentImage));
            gallery.setAdapter(new CustomerGalleryAdapter(getContext(), customer.getImage()));
            gallery.invalidate();

            ProgresService.getInstance().showProgress(getContext(), getString(R.string.customer_saving));
            NetworkService
                    .getInstance()
                    .getCustomerApi()
                    .doPutCustomer(customer)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            ProgresService.getInstance().hideProgress();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
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

            customer.setProperty(PropertyService.getInstance().getPropertyFromList(listView));
            ProgresService.getInstance().showProgress(getContext(), getString(R.string.customer_saving));
            NetworkService
                    .getInstance()
                    .getCustomerApi()
                    .doPutCustomer(customer)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            ProgresService.getInstance().hideProgress();
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

            String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
            if (ContextCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_GRANTED) {

                Intent ringIntent = new Intent();
                ringIntent.setType("image/*");
                ringIntent.setAction(Intent.ACTION_GET_CONTENT);
                ringIntent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(ringIntent, "Select Image"), PICK_IMAGE_SELECTED);
            }
            else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, STORAGE_PERMISSION_CODE);
            }
        });

        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Toast
                .makeText(getActivity(), "bbb", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case PICK_IMAGE_SELECTED: {
                if (resultCode == Activity.RESULT_OK) {
                    Customer customer = MemoryService.getInstance().getCustomer();
                    try {
                        byte[] data = ImageService.getInstance().getImage(getContext(), imageReturnedIntent);
                        ProgresService.getInstance().showProgress(getContext(), getString(R.string.customer_saving));
                        NetworkService
                                .getInstance()
                                .getImageApi()
                                .doPostImage(data)
                                .enqueue(new Callback<Long>() {
                                    @Override
                                    public void onResponse(Call<Long> call, Response<Long> response) {
                                        ProgresService.getInstance().hideProgress();
                                        customer.getImage().add(response.body());
                                        ProgresService.getInstance().showProgress(getContext(), getString(R.string.customer_saving));
                                        NetworkService
                                                .getInstance()
                                                .getCustomerApi()
                                                .doPutCustomer(customer)
                                                .enqueue(new Callback<Void>() {
                                                    @Override
                                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                                        ProgresService.getInstance().hideProgress();
                                                        gallery.setAdapter(new CustomerGalleryAdapter(getContext(), customer.getImage()));
                                                        gallery.invalidate();
                                                        if (customer.getImage().size() != 0)
                                                            root.findViewById(R.id.buttonDelete).setEnabled(true);
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Void> call, Throwable t) {
                                                        ProgresService.getInstance().hideProgress();
                                                        Toast
                                                                .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                                                .show();
                                                    }
                                                });
                                    }

                                    @Override
                                    public void onFailure(Call<Long> call, Throwable t) {
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
