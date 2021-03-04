package com.rental.transport.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.model.Customer;
import com.rental.transport.service.FragmentService;
import com.rental.transport.service.MemoryService;
import com.rental.transport.service.NetworkService;
import com.rental.transport.service.ProgresService;
import com.rental.transport.service.SharedService;
import com.rental.transport.validator.EmailValidator;
import com.rental.transport.validator.IStringValidator;
import com.rental.transport.validator.PasswordValidator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerLogin extends Fragment {

    private Boolean isValidEmail(EditText text) {

        IStringValidator validator = new EmailValidator();
        if (validator.validate(text.getText().toString())) {
            return true;
        }

        text.requestFocus();
        text.setError(getString(R.string.wrong_data_entered));
        return false;
    }

    private Boolean isValidPassword(EditText text) {
        IStringValidator validator = new PasswordValidator();
        if (validator.validate(text.getText().toString())) {
            return true;
        }

        text.requestFocus();
        text.setError(getString(R.string.wrong_data_entered));
        return false;
    }

    void login(View root, String username, String password) {
        ProgresService
                .getInstance()
                .showProgress(getActivity(), getString(R.string.customer_loading));

        NetworkService
                .getInstance(username, password)
                .getCustomerApi()
                .doGetCustomerRequest()
                .enqueue(new Callback<Customer>() {
                    @Override
                    public void onResponse(Call<Customer> call, Response<Customer> response) {
                        ProgresService.getInstance().hideProgress();

                        if (response.code() == 401) {
                            SharedService.getInstance().clear();
                            Toast
                                    .makeText(getContext(), getString(R.string.wrong_credentials), Toast.LENGTH_LONG)
                                    .show();
                        }

                        if (response.isSuccessful()) {
                            CheckBox remember = root.findViewById(R.id.loginAuto);
                            if (remember.isChecked()) {
                                SharedService.getInstance().setValue(getActivity(), getString(R.string.preferred_username), username);
                                SharedService.getInstance().setValue(getActivity(), getString(R.string.preferred_password), password);
                            }
                            MemoryService.getInstance().setCustomer(response.body());
                            ((MainActivity) getActivity()).showMenu(true);
                            FragmentService.getInstance().load(getActivity(), "TransportFragment");
                            remember.clearFocus();
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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.customer_login, container, false);
        EditText customer = root.findViewById(R.id.loginEmail);
        EditText password = root.findViewById(R.id.loginPassword);

        TextView loginForgotPasswordLink = root.findViewById(R.id.loginForgotPasswordLink);
        loginForgotPasswordLink.setPaintFlags(loginForgotPasswordLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        TextView loginRegisterLink = root.findViewById(R.id.loginRegisterLink);
        loginRegisterLink.setPaintFlags(loginRegisterLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        root.findViewById(R.id.loginButton).setOnClickListener(v -> {
            if (isValidEmail(customer) && isValidPassword(password)) {
                login(root,
                        customer.getText().toString(),
                        password.getText().toString()
                );
            }
        });

        root.findViewById(R.id.loginForgotPasswordLink).setOnClickListener(v -> {
            if (isValidEmail(customer)) {

                ProgresService.
                        getInstance()
                        .showProgress(getActivity(), getString(R.string.request));

                NetworkService
                        .getInstance()
                        .getRegistrationApi()
                        .doPostEmailRegistration(customer.getText().toString())
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                ProgresService.getInstance().hideProgress();
                                if (response.isSuccessful()) {
                                    Toast
                                            .makeText(getContext(), getString(R.string.check_email), Toast.LENGTH_LONG)
                                            .show();
                                } else {
                                    Toast
                                            .makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG)
                                            .show();
                                }
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

        root.findViewById(R.id.loginRegisterLink).setOnClickListener(v -> FragmentService
                .getInstance()
                .load(getActivity(), "CustomerCreate"));

        String savedUsername = SharedService.getInstance().getValue(getActivity(), getString(R.string.preferred_username));
        String savedPassword = SharedService.getInstance().getValue(getActivity(), getString(R.string.preferred_password));

        if ((savedUsername != null) && (savedPassword != null))

            login(root,
                    savedUsername,
                    savedPassword
            );

        ((MainActivity) getActivity()).showMenu(false);
        return root;
    }
}