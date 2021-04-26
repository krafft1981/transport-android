package com.rental.transport.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.adapter.CustomerGalleryAdapter;
import com.rental.transport.adapter.PropertyListAdapter;
import com.rental.transport.adapter.TransportGalleryAdapter;
import com.rental.transport.model.Customer;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;
import com.rental.transport.service.PropertyService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerSettings extends Fragment {

    private int currentImage = 0;
    private final int Pick_image = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Customer customer = MemoryService.getInstance().getCustomer();

        View root = inflater.inflate(R.layout.customer_settings, container, false);
        ListView listView = root.findViewById(R.id.property);
        PropertyListAdapter adapter = new PropertyListAdapter(getContext(), customer.getProperty(), true);
        listView.setAdapter(adapter);

        Gallery gallery = root.findViewById(R.id.gallery);
        gallery.setAdapter(new CustomerGalleryAdapter(getContext(), customer.getImage(), true));
        gallery.setOnItemClickListener((parent, view, position, id) -> {
            MemoryService.getInstance().setImageId(customer.getImage().get(position));
            FragmentService.getInstance().load(getActivity(), "PictureFragment");
        });

        if (customer.getImage().size() == 0)
            root.findViewById(R.id.buttonDelete).setEnabled(false);

        root.findViewById(R.id.buttonLoad).setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, Pick_image);
        });

        root.findViewById(R.id.buttonDelete).setOnClickListener(v -> {

            customer.getImage().remove(customer.getImage().get(currentImage));
            gallery.setAdapter(new TransportGalleryAdapter(getContext(), customer.getImage()));
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
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, Pick_image);
        });

        return root;
    }

    //Обрабатываем результат выбора в галерее:
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case Pick_image:
                if (resultCode == Activity.RESULT_OK) {
                    Customer customer = MemoryService.getInstance().getCustomer();
                    try {
                        final String fileName = imageReturnedIntent.getData().getEncodedPath();
                        // save image
                        // append image to customer
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
    }
}
