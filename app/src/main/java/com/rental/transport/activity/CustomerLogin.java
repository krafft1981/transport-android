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
import com.rental.transport.service.NetworkService;
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
        ((MainActivity) getActivity()).showProgress(getString(R.string.customer_loading));
        NetworkService
                .getInstance(username, password)
                .getCustomerApi()
                .doGetCustomerRequest()
                .enqueue(new Callback<Customer>() {
                    @Override
                    public void onResponse(Call<Customer> call, Response<Customer> response) {
                        ((MainActivity) getActivity()).hideProgress();
                        if (response.code() == 401) {
                            SharedService
                                    .getInstance(getActivity())
                                    .clear();
                            Toast
                                    .makeText(getContext(), getString(R.string.wrong_credentials), Toast.LENGTH_LONG)
                                    .show();
                        }

                        if (response.isSuccessful()) {
                            CheckBox remember = root.findViewById(R.id.loginAuto);
                            if (remember.isChecked()) {
                                SharedService
                                        .getInstance(getActivity())
                                        .save(username, password);
                            }

                            Customer customer = response.body();
                            ((MainActivity) getActivity()).setCustomer(customer);
                            ((MainActivity) getActivity()).showMenu(true);
                            ((MainActivity) getActivity()).loadFragment("TransportFragment");
                        }
                    }

                    @Override
                    public void onFailure(Call<Customer> call, Throwable t) {
                        ((MainActivity) getActivity()).hideProgress();
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

//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            String msg = bundle.getString("name");
//            if (msg != null) {
//            }
//        }

        View root = inflater.inflate(R.layout.customer_login, container, false);
        EditText customer = root.findViewById(R.id.loginEmail);
        EditText password = root.findViewById(R.id.loginPassword);

        TextView loginForgotPasswordLink = root.findViewById(R.id.loginForgotPasswordLink);
        loginForgotPasswordLink.setPaintFlags(loginForgotPasswordLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        TextView loginRegisterLink = root.findViewById(R.id.loginRegisterLink);
        loginRegisterLink.setPaintFlags(loginRegisterLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        root.findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidEmail(customer) && isValidPassword(password)) {
                    login(root,
                            customer.getText().toString(),
                            password.getText().toString()
                    );
                }
            }
        });

        root.findViewById(R.id.loginForgotPasswordLink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidEmail(customer)) {

                    NetworkService
                            .getInstance()
                            .getRegistrationApi()
                            .doPostEmailRegistration(customer.getText().toString())
                            .enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    ((MainActivity) getActivity()).hideProgress();
                                    if (response.isSuccessful()) {
                                        Toast
                                                .makeText(getContext(), getString(R.string.check_email), Toast.LENGTH_LONG)
                                                .show();
                                    }
                                    else {
                                        Toast
                                                .makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG)
                                                .show();
                                    }
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
            }
        });

        root.findViewById(R.id.loginRegisterLink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).loadFragment("CustomerCreate");
            }
        });

        String savedUsername = SharedService.getInstance(getActivity()).getUsername();
        String savedPassword = SharedService.getInstance(getActivity()).getPassword();

        if ((savedUsername != null) && (savedPassword != null))

            login(root,
                    savedUsername,
                    savedPassword
            );

        return root;
    }
}
