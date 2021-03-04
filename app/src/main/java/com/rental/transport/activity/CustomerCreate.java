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
import com.rental.transport.validator.EmailValidator;
import com.rental.transport.validator.IStringValidator;
import com.rental.transport.validator.PasswordValidator;
import com.rental.transport.validator.PhoneValidator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerCreate extends Fragment {

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

    private Boolean isValidPhone(EditText text) {
        IStringValidator validator = new PhoneValidator();
        if (validator.validate(text.getText().toString())) {
            return true;
        }

        text.requestFocus();
        text.setError(getString(R.string.wrong_data_entered));
        return false;
    }

    private Boolean isValidFio(EditText text) {
        IStringValidator validator = new PasswordValidator();
        if (validator.validate(text.getText().toString())) {
            return true;
        }

        text.requestFocus();
        text.setError(getString(R.string.wrong_data_entered));
        return false;
    }


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
            if (!isValidEmail(customer)) {
                customer.setError(getResources().getString(R.string.error));
                return;
            }
            if (!isValidPassword(password)) {
                password.setError(getResources().getString(R.string.error));
                return;
            }
            if (!isValidPhone(phone)) {
                phone.setError(getResources().getString(R.string.error));
                return;
            }
            if (!isValidFio(fio)) {
                phone.setError(getResources().getString(R.string.error));
                return;
            }

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
                            if (response.isSuccessful())
                                FragmentService.getInstance().load(getActivity(), "CustomerLogin");
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
