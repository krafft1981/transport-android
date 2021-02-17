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
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.model.Customer;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.ImageService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;
import com.rental.transport.service.PropertyService;
import com.rental.transport.service.SharedService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerSettings extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Customer customer = MemoryService.getInstance().getCustomer();

        View root = inflater.inflate(R.layout.customer_settings, container, false);
        TableLayout table = root.findViewById(R.id.propertyTable);
//        PropertyService.getInstance()
//                .setPropertyToView(table, new ArrayList(customer.getProperty()), true)
//                .setPropertyToView(table, new Property(getString(R.string.transport), "transport_count", String.valueOf(customer.getTransport().size())))
//                .setPropertyToView(table, new Property(getString(R.string.parking), "parking_count", String.valueOf(customer.getParking().size())))
//                .setPropertyToView(table, new Property(getString(R.string.image), "image_count", String.valueOf(customer.getImage().size())));

        LinearLayout buttonLayout = root.findViewById(R.id.buttonLayout);

        Button save = new Button(getContext());
        save.setText(getString(R.string.save));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customer.setProperty(
                        PropertyService
                                .getInstance()
                                .getPropertyFromTable(table)
                );

                ProgresService.getInstance().showProgress(getString(R.string.customer_saving));

                NetworkService
                        .getInstance()
                        .getCustomerApi()
                        .doPutUpdateCustomerRequest(customer)
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

        buttonLayout.addView(save);

        Button exit = new Button(getContext());
        exit.setText(getString(R.string.exit));
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedService.getInstance().clear();
                ((MainActivity) getActivity()).showMenu(false);
                FragmentService.getInstance().fragmentHistoryClear();
                FragmentService.getInstance().loadFragment("CustomerLogin");

            }
        });

        buttonLayout.addView(exit);

        LinearLayout images = root.findViewById(R.id.customerImages);
        ImageService
                .getInstance(getContext())
                .setImage(customer.getImage(), R.drawable.unnamed, images, true)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        try {
//                            Intent i = new Intent(Intent.ACTION_PICK,
//                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                            startActivityForResult(i, ((MainActivity) getActivity()).RESULT_LOAD_IMAGE);
//                        } catch (Exception exp) {
//
//                        }
                    }
                });

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);

        HorizontalScrollView scrollView = root.findViewById(R.id.horizontalScroll);
        scrollView.setClipToPadding(false);
        android.view.ViewGroup.LayoutParams layoutParams = scrollView.getLayoutParams();
        layoutParams.height = size.y / 2;
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
