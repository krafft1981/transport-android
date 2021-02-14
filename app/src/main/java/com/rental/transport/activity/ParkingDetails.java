package com.rental.transport.activity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.model.Customer;
import com.rental.transport.model.Parking;
import com.rental.transport.model.Property;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.ImageService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;
import com.rental.transport.service.PropertyService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingDetails extends Fragment {

    private Integer page = 0;
    private Integer size = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            String msg = bundle.getString("name");
            if (msg != null) {
            }
        }

        View root = inflater.inflate(R.layout.parking_details, container, false);
        Parking parking = MemoryService.getInstance().getParking();
        Customer customer = MemoryService.getInstance().getCustomer();
        TableLayout table = root.findViewById(R.id.propertyTable);
        LinearLayout buttonLayout = root.findViewById(R.id.buttonLayout);
        Boolean editable = parking.getCustomer().contains(customer.getId());
        PropertyService
                .getInstance()
                .setPropertyToTable(table, new ArrayList(parking.getProperty()), editable)
                .setPropertyToTable(table, new Property(getString(R.string.transport), "transport_count", String.valueOf(parking.getTransport().size())))
                .setPropertyToTable(table, new Property(getString(R.string.customer), "customer_count", String.valueOf(parking.getCustomer().size())))
                .setPropertyToTable(table, new Property(getString(R.string.image), "image_count", String.valueOf(parking.getImage().size())));

        if (editable) {
            Button action = new Button(getContext());
            action.setText(getString(R.string.save));
            action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parking.setProperty(
                            PropertyService
                                    .getInstance()
                                    .getPropertyFromTable(table)
                    );

                    ProgresService.getInstance().showProgress(getString(R.string.parking_saving));
                    NetworkService
                            .getInstance()
                            .getParkingApi()
                            .doPutParking(parking)
                            .enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    ProgresService.getInstance().hideProgress();
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    ProgresService.getInstance().hideProgress();
                                    Toast
                                            .makeText(getContext(), t.toString(), Toast.LENGTH_LONG)
                                            .show();
                                }
                            });

                }
            });

            buttonLayout.addView(action);
        }

        Button map = new Button(getContext());
        map.setText(getString(R.string.map));
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentService.getInstance().loadFragment("MapFragment");
            }
        });

        buttonLayout.addView(map);

        LinearLayout images = root.findViewById(R.id.parking_images);
        ImageView image = ImageService
                .getInstance()
                .setImage(parking.getImage(), R.drawable.unnamed, images, editable);

        if (editable) {
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    try {
//                        Intent i = new Intent(Intent.ACTION_PICK,
//                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        startActivityForResult(i, ((MainActivity) getActivity()).RESULT_LOAD_IMAGE);
//                    } catch (Exception exp) {
//
//                    }
                }
            });
        }

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getRealSize(point);

        HorizontalScrollView scrollView = root.findViewById(R.id.horizontalScrol);
        scrollView.setClipToPadding(false);
        android.view.ViewGroup.LayoutParams layoutParams = scrollView.getLayoutParams();
        layoutParams.height = ((point.y / 3) * 2);
        scrollView.setLayoutParams(layoutParams);

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Toast
                .makeText(getContext(), "Картинка выбрана", Toast.LENGTH_LONG)
                .show();
    }
}
