package com.rental.transport.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rental.transport.R;
import com.rental.transport.model.Customer;
import com.rental.transport.service.NetworkService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private String getAccount() {
        Context context = getApplicationContext();
        AccountManager accountManager = AccountManager.get(context);
        List<Account> accounts = Arrays.asList(accountManager.getAccountsByType("com.google"));
        if (accounts.size() == 0) {

            Toast
                    .makeText(getApplicationContext(), "Не шмогла я аккаунт добыть (((", Toast.LENGTH_LONG)
                    .show();

            return "";
        }

        Toast
                .makeText(getApplicationContext(), "Используем аккаунт: " + accounts.get(0).name, Toast.LENGTH_LONG)
                .show();

        return accounts.get(0).name;
    }

    Boolean checkPermissions() {

        String[] permissions  = {
                Manifest.permission.INTERNET,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }

        return true;
    }

    public void loadFragment(String name) {
        Fragment fragment = fragmentMap.get(name);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.setCustomAnimations(
                R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                R.animator.card_flip_left_in, R.animator.card_flip_left_out);
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    private void showProgress(String text) {

        if (progressDialog == null) {
            try {
                progressDialog = ProgressDialog.show(this, "", text);
                progressDialog.setCancelable(false);
            } catch (Exception e) {

            }
        }
    }

    public void hideProgress() {

        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private ProgressDialog progressDialog = null;

    public BottomNavigationView bottomNavigationView;

    public Map<String, Fragment> fragmentMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (checkPermissions() == false) {
            // exit
        }

        String account = getAccount();

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showProgress("Загрузка аккаунта");

        NetworkService
                .getInstance(account)
                .getRegistrationApi()
                .doPostRegistration(account)
                .enqueue(new Callback<Long>() {
                    @Override
                    public void onResponse(@NonNull Call<Long> call, @NonNull Response<Long> response) {

                        Long userId = response.body();
                        NetworkService
                                .getInstance()
                                .getCustomerApi()
                                .doGetCustomer()
                                .enqueue(new Callback<Customer>() {
                                    @Override
                                    public void onResponse(@NonNull Call<Customer> call, @NonNull Response<Customer> response) {

                                        hideProgress();
                                        Customer customer = response.body();

                                        if (customer.getFirstName().isEmpty() ||
                                                customer.getLastName().isEmpty() ||
                                                customer.getFamily().isEmpty() ||
//                                                customer.getImages().isEmpty() ||
                                                customer.getPhone().isEmpty()) {

//                                            loadFragment("CustomerSettings");
                                        }
                                        else {

                                            bottomNavigationView.setVisibility(View.VISIBLE);
//                                            loadFragment("TransportFragment");
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<Customer> call, @NonNull Throwable t) {

                                        Toast
                                                .makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG)
                                                .show();

                                        hideProgress();
                                        finish();
                                    }
                                });

                    }

                    @Override
                    public void onFailure(@NonNull Call<Long> call, @NonNull Throwable t) {

                        Toast
                                .makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG)
                                .show();

                        hideProgress();
                        finish();
                    }
                });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.transport_menu:
//                                loadFragment("TransportFragment");
                                break;

                            case R.id.orders_menu:
//                                loadFragment("OrdersFragment");
                                break;

                            case R.id.account_menu:
//                                loadFragment("CustomerSetting");
                                break;
                        }

                        return true;
                    }
                });
    }
}
