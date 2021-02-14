package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;

public class OrdersFragment extends Fragment {

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

        View root = inflater.inflate(R.layout.order_fragment, container, false);
        ((MainActivity) getActivity()).showProgress(getString(R.string.orders_loading));
//        NetworkService
//                .getInstance()
//                .getOrderApi()
//                .doGetCustomerOrders()
//                .enqueue(new Callback<Void>() {
//                    @Override
//                    public void onResponse(Call<Void> call, Response<Void> response) {
//
//                        ((MainActivity) getActivity()).hideProgress();
//                        if (response.isSuccessful()) {
//                            ((MainActivity) getActivity()).showMenu(true);
//                            ((MainActivity) getActivity()).loadFragment("TransportFragment");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Void> call, Throwable t) {
//
//                        ((MainActivity) getActivity()).hideProgress();
//                        Toast
//                                .makeText(getContext(), t.toString(), Toast.LENGTH_LONG)
//                                .show();
//                    }
//                });
        ((MainActivity) getActivity()).hideProgress();
        return root;
    }
}
