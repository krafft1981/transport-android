package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.model.Customer;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;
import com.rental.transport.service.SharedService;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerCreate extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.customer_create, container, false);
        EditText customer = root.findViewById(R.id.fieldCustomerEmail);
        EditText password = root.findViewById(R.id.fieldCustomerPassword);
        EditText phone = root.findViewById(R.id.fieldCustomerPhone);
        EditText fio = root.findViewById(R.id.fieldCustomerFio);
        root.findViewById(R.id.createButton).setOnClickListener(v -> {
            ProgresService.getInstance().showProgress(getContext(), getString(R.string.customer_creating));
            NetworkService
                    .getInstance()
                    .getRegistrationApi()
                    .doPostRegistration(
                            customer.getText().toString(),
                            password.getText().toString(),
                            phone.getText().toString(),
                            fio.getText().toString()
                    )
                    .enqueue(new Callback<Customer>() {
                        @Override
                        public void onResponse(Call<Customer> call, Response<Customer> response) {
                            ProgresService.getInstance().hideProgress();
                            if (response.isSuccessful()) {
                                FragmentService.getInstance().load(getActivity(), "CustomerLogin");
                                SharedService.getInstance().setValue(getActivity(), getString(R.string.preferred_username), customer.getText().toString());
                                SharedService.getInstance().setValue(getActivity(), getString(R.string.preferred_password), password.getText().toString());
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
                        public void onFailure(Call<Customer> call, Throwable t) {
                            ProgresService.getInstance().hideProgress();
                            Toast
                                    .makeText(getContext(), t.toString(), Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
        });

        return root;
    }
}
