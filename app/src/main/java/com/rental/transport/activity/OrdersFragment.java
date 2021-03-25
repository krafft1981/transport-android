package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rental.transport.R;
import com.rental.transport.adapter.OrderConfirmationListAdapter;
import com.rental.transport.model.Order;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;
import com.rental.transport.views.FabExpander;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends Fragment {

    private Integer page = 0;
    private Integer size = 200;

    private FabExpander expander_add;
    private FabExpander expander_sub;
    private FabExpander expander_save;

    private boolean fabStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fabStatus = false;

        View root = inflater.inflate(R.layout.order_fragment, container, false);
        ListView orderList = root.findViewById(R.id.orders_list);
        ProgresService.getInstance().showProgress(getActivity(), getString(R.string.orders_loading));
        NetworkService
                .getInstance()
                .getOrderApi()
                .doGetPagesOrderRequest(page, size)
                .enqueue(new Callback<List<Order>>() {
                    @Override
                    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {

                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful())
                            orderList.setAdapter(new OrderConfirmationListAdapter(getActivity(), response.body()));
                    }

                    @Override
                    public void onFailure(Call<List<Order>> call, Throwable t) {

                        ProgresService.getInstance().hideProgress();
                        Toast
                                .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });

        expander_add = new FabExpander(
                root.findViewById(R.id.floating_action_add_button),
                AnimationUtils.loadAnimation(getContext(), R.anim.fab_top_show),
                AnimationUtils.loadAnimation(getContext(), R.anim.fab_top_hide),
                1.7, 0.25
        );

        expander_sub = new FabExpander(
                root.findViewById(R.id.floating_action_sub_button),
                AnimationUtils.loadAnimation(getContext(), R.anim.fab_middle_show),
                AnimationUtils.loadAnimation(getContext(), R.anim.fab_middle_hide),
                1.5, 1.5
        );

        expander_save = new FabExpander(
                root.findViewById(R.id.floating_action_save_button),
                AnimationUtils.loadAnimation(getContext(), R.anim.fab_bottom_show),
                AnimationUtils.loadAnimation(getContext(), R.anim.fab_bottom_hide),
                0.25, 1.7
        );

        FloatingActionButton fab = root.findViewById(R.id.floatingActionButton);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(view -> {
            if (fabStatus) {
                expander_add.hide();
                expander_sub.hide();
                expander_save.hide();
                fabStatus = false;
            } else {
                expander_add.expand();
                expander_sub.expand();
                expander_save.expand();
                fabStatus = true;
            }
        });

        root.findViewById(R.id.floating_action_add_button).setOnClickListener(view -> {
            expander_add.hide();
            expander_sub.hide();
            expander_save.hide();
            fabStatus = false;
        });

        root.findViewById(R.id.floating_action_sub_button).setOnClickListener(view -> {
            expander_add.hide();
            expander_sub.hide();
            expander_save.hide();
            fabStatus = false;
        });

        root.findViewById(R.id.floating_action_save_button).setOnClickListener(view -> {
            expander_add.hide();
            expander_sub.hide();
            expander_save.hide();
            fabStatus = false;
        });

        return root;
    }
}
