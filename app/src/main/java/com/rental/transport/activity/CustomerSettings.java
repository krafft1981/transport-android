package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.adapter.CustomerGalleryAdapter;
import com.rental.transport.adapter.PropertyListAdapter;
import com.rental.transport.model.Customer;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.PropertyService;

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
        ListView listView = root.findViewById(R.id.property);
        PropertyListAdapter adapter = new PropertyListAdapter(getContext(), customer.getProperty(), true);
        listView.setAdapter(adapter);

        Gallery gallery = root.findViewById(R.id.gallery);
        gallery.setAdapter(new CustomerGalleryAdapter(getContext(), customer.getImage(), true));
        gallery.setOnItemClickListener((parent, view, position, id) -> {
            MemoryService.getInstance().setImageId(customer.getImage().get(position));
            FragmentService.getInstance().load(getActivity(), "PictureFragment");
        });

            customer.setProperty(PropertyService.getInstance().getPropertyFromList(listView));

//            ProgresService.getInstance().showProgress(getContext(), getString(R.string.customer_saving));
//            NetworkService
//                    .getInstance()
//                    .getCustomerApi()
//                    .doPutUpdateCustomerRequest(customer)
//                    .enqueue(new Callback<Void>() {
//                        @Override
//                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
//                            ProgresService.getInstance().hideProgress();
//                        }
//
//                        @Override
//                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
//                            ProgresService.getInstance().hideProgress();
//                            Toast
//                                    .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
//                                    .show();
//                        }
//                    });

//            SharedService.getInstance().clear();
//            FragmentService.getInstance().fragmentHistoryClear(getActivity());
//            FragmentService.getInstance().load(getActivity(), "CustomerLogin");

        return root;
    }
}
