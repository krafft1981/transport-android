package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.adapter.OrderChatAdapter;
import com.rental.transport.model.Order;
import com.rental.transport.model.Text;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;

import org.json.JSONObject;

import java.util.Date;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetails extends Fragment {

    private static final String format = "d MMMM (EEE)";
    private android.text.format.DateFormat df = new android.text.format.DateFormat();

    private void sendOrderMessage(ListView list, EditText source, Long orderId) {

        ProgresService.getInstance().showProgress(getContext(), getContext().getString(R.string.events_loading));
        NetworkService
                .getInstance()
                .getOrderApi()
                .doPostMessage(orderId, new Text(source.getText().toString()))
                .enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful()) {
                            list.setAdapter(new OrderChatAdapter(getContext(), response.body().getMessage()));
                            list.invalidate();
                            source.setText("");
                        }
                        else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast
                                        .makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG)
                                        .show();
                            }
                            catch (Exception e) {
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                        ProgresService.getInstance().hideProgress();
                        Toast
                                .makeText(getContext(), t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    private void updateOrder(ListView list, Long orderId) {

        ProgresService.getInstance().showProgress(getContext(), getContext().getString(R.string.events_loading));
        NetworkService
                .getInstance()
                .getOrderApi()
                .doGetOrder(orderId)
                .enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                        ProgresService.getInstance().hideProgress();
                        if (response.isSuccessful()) {
                            list.setAdapter(new OrderChatAdapter(getContext(), response.body().getMessage()));
                            list.invalidate();
                        }
                        else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast
                                        .makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG)
                                        .show();
                            }
                            catch (Exception e) {
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                        ProgresService.getInstance().hideProgress();
                        Toast
                                .makeText(getContext(), t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Order order = MemoryService.getInstance().getOrder();

        View root = inflater.inflate(R.layout.order_details, container, false);
        TextView day = root.findViewById(R.id.orderDay);
        day.setText(df.format(format, new Date(order.getDay())));
        TextView hours = root.findViewById(R.id.orderHours);
        hours.setText(order.getMinHour() + ":00" + " - " + order.getMaxHour() + ":00");

        ListView list = root.findViewById(R.id.orderMessageChat);
        list.setAdapter(new OrderChatAdapter(getContext(), order.getMessage()));

        EditText source = root.findViewById(R.id.chatMessageBody);

        root.findViewById(R.id.chatMessageSend).setOnClickListener(v -> sendOrderMessage(list, source, order.getId()));
        root.findViewById(R.id.chatMessageListUpdate).setOnClickListener(v -> updateOrder(list, order.getId()));
        return root;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
