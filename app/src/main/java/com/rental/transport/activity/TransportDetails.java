package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.model.Customer;
import com.rental.transport.model.Transport;
import com.rental.transport.service.MemoryService;

public class TransportDetails extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.transport_details, container, false);

        Transport transport = MemoryService.getInstance().getTransport();
        Customer customer = MemoryService.getInstance().getCustomer();

        Boolean editable = transport.getCustomer().contains(customer.getId());

        ImageSwitcher switcher = root.findViewById(R.id.imageSwitcher);
        switcher.setImageResource(R.drawable.unnamed);
        switcher.setInAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left));
        switcher.setOutAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right));
        switcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getContext());
                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                myView.setLayoutParams(
                        new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                );
                return myView;
            }
        });

//        ArrayList propertyList = new ArrayList(transport.getProperty());

//        PropertyListAdapter adapter = new PropertyListAdapter(getContext(), propertyList);

//        PropertyService
//                .getInstance()
//                .setPropertyToList(listView, new ArrayList(transport.getProperty()), editable)
//                .setPropertyToList(listView, new Property(getString(R.string.customer), "customer_count", String.valueOf(transport.getCustomer().size())))
//                .setPropertyToList(listView, new Property(getString(R.string.image), "image_count", String.valueOf(transport.getImage().size())));

//        LinearLayout images = root.findViewById(R.id.transportImages);
//        ImageView image = ImageService
//                .getInstance()
//                .setImage(transport.getImage(), R.drawable.transport, images, editable);
//
//        if (editable) {
//            image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    try {
////                        Intent i = new Intent(Intent.ACTION_PICK,
////                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////                        startActivityForResult(i, ((MainActivity) getActivity()).RESULT_LOAD_IMAGE);
////                    } catch (Exception exp) {
////
////                    }
//                }
//            });
//        }

//        Button action = new Button(getContext());
//        if (!editable) {
//            action.setText(getString(R.string.toOrder));
//            action.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    FragmentService
//                            .getInstance()
//                            .loadFragment(getActivity(), "CalendarFragment");
//
//                }
//            });
//        }
//        else {
//            action.setText(getString(R.string.save));
//            action.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    transport.setProperty(
////                            PropertyService
////                                    .getInstance()
////                                    .getPropertyFromTable(view)
////                    );
//
//                    ProgresService
//                            .getInstance()
//                            .showProgress(getContext(), getString(R.string.transport_saving));
//
//                    NetworkService
//                            .getInstance()
//                            .getTransportApi()
//                            .doPutTransport(transport)
//                            .enqueue(new Callback<Void>() {
//                                @Override
//                                public void onResponse(Call<Void> call, Response<Void> response) {
//                                    ProgresService.getInstance().hideProgress();
//                                }
//
//                                @Override
//                                public void onFailure(Call<Void> call, Throwable t) {
//                                    ProgresService.getInstance().hideProgress();
//                                    Toast
//                                            .makeText(getContext(), t.toString(), Toast.LENGTH_LONG)
//                                            .show();
//                                }
//                            });
//                }
//            });
//        }
//
//        buttonLayout.addView(action);

//        Display display = getActivity().getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getRealSize(size);
//
//        HorizontalScrollView scrollView = root.findViewById(R.id.horizontalScroll);
//        scrollView.setClipToPadding(false);
//        ViewGroup.LayoutParams layoutParams = scrollView.getLayoutParams();
//        layoutParams.height = ((size.y / 3) * 2);
//        scrollView.setLayoutParams(layoutParams);

        return root;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        switch (requestCode) {
//            case 0:
//                if (resultCode == RESULT_OK) {
////                    Uri selectedImage = imageReturnedIntent.getData();
////                    imageview.setImageURI(selectedImage);
//                }
//
//                break;
//            case 1:
//                if (resultCode == RESULT_OK) {
////                    Uri selectedImage = imageReturnedIntent.getData();
////                    imageview.setImageURI(selectedImage);
//                }
//                break;
//        }
//    }
}
