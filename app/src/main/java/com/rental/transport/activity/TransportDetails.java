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
import com.rental.transport.model.Property;
import com.rental.transport.model.Transport;
import com.rental.transport.service.ImageService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.PropertyService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransportDetails extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            String msg = bundle.getString("name");
//            if (msg != null) {
//            }
//        }

        View root = inflater.inflate(R.layout.transport_details, container, false);
        Transport transport = ((MainActivity) getActivity()).getTransport();
        Customer customer = ((MainActivity) getActivity()).getCustomer();
        TableLayout table = root.findViewById(R.id.tableProperty);
        LinearLayout buttonLayout = root.findViewById(R.id.buttonLayout);
        Boolean editable = transport.getCustomer().contains(customer.getId());

        System.out.println(transport.getCustomer() + " " + customer.getId());

        PropertyService
                .getInstance(getContext())
                .setPropertyToTable(
                        table,
                        transport.getProperty(),
                        editable
                );

        PropertyService
                .getInstance(getContext())
                .addTableRow(
                        table,
                        new Property(
                                getString(R.string.customer),
                                "customer_count",
                                String.valueOf(transport.getCustomer().size())
                        ),
                        false
                );

        PropertyService
                .getInstance(getContext())
                .addTableRow(
                        table,
                        new Property(
                                getString(R.string.image),
                                "image_count",
                                String.valueOf(transport.getImage().size()
                                )
                        ),
                        false
                );

        LinearLayout images = root.findViewById(R.id.transport_images);
        ImageView image = ImageService
                .getInstance(getContext())
                .setImage(transport.getImage(), R.drawable.samokat, images, editable);

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

        Button action = new Button(getContext());
        action.setBackground(this
                .getResources()
                .getDrawable(R.drawable.border)
        );

        if (!editable) {
            action.setText(getString(R.string.toOrder));
            action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).setTransport(transport);
                    ((MainActivity) getActivity()).loadFragment("CalendarFragment");
                }
            });
        }
        else {
            action.setText(getString(R.string.save));
            action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transport.setProperty(
                            PropertyService
                                    .getInstance(getContext())
                                    .getPropertyFromTable(table)
                    );

                    ((MainActivity) getActivity()).showProgress(getString(R.string.transport_saving));

                    NetworkService
                            .getInstance()
                            .getTransportApi()
                            .doPutTransport(transport)
                            .enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {

                                    ((MainActivity) getActivity()).hideProgress();
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {

                                    ((MainActivity) getActivity()).hideProgress();
                                    Toast
                                            .makeText(getContext(), t.toString(), Toast.LENGTH_LONG)
                                            .show();
                                }
                            });
                }
            });
        }

        buttonLayout.addView(action);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);

        HorizontalScrollView scrollView = root.findViewById(R.id.horizontalScrol);
        scrollView.setClipToPadding(false);
        android.view.ViewGroup.LayoutParams layoutParams = scrollView.getLayoutParams();
        layoutParams.height = ((size.y / 3) * 2);
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
