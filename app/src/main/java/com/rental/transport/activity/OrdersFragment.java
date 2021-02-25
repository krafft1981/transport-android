package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.adapter.OrderListAdapter;
import com.rental.transport.adapter.ParkingGridAdapter;
import com.rental.transport.model.Order;
import com.rental.transport.model.Parking;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.order_fragment, container, false);
        ListView orderList = root.findViewById(R.id.orders_list);
        ProgresService
                .getInstance()
                .showProgress(getActivity(), getString(R.string.orders_loading));
        NetworkService
                .getInstance()
                .getOrderApi()
                .doGetCustomerOrders()
                .enqueue(new Callback<Set<Order>>() {
                    @Override
                    public void onResponse(Call<Set<Order>> call, Response<Set<Order>> response) {

                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful()) {
                            Set<Order> orders = response.body();
                            if (orders != null) {
                                OrderListAdapter adapter = new OrderListAdapter(getActivity(), new ArrayList(orders));
                                orderList.setAdapter(adapter);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Set<Order>> call, Throwable t) {

                        ProgresService.getInstance().hideProgress();
                        Toast
                                .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });

        return root;
    }
}
