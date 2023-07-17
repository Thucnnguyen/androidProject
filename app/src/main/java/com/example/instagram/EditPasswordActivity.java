package com.example.instagram;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.instagram.model.Customer;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditPasswordActivity extends AppCompatActivity {

    private Button btnReturn, btnConfirm;
    private EditText etxtCurrent, etxtNew, etxtConfirm;
    private int customerId;

    private Customer Customer;

    private boolean check ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_password);

        etxtCurrent = (EditText) findViewById(R.id.edit_current_password);
        etxtNew = (EditText) findViewById(R.id.edit_new_password);
        etxtConfirm = (EditText) findViewById(R.id.edit_confirm_password);

        btnReturn = (Button) findViewById(R.id.edit_profile_return);
        btnConfirm = (Button) findViewById(R.id.password_edit_confirm);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePassword(v);
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE);
        customerId = sharedPreferences.getInt("customerId",0);
        if (customerId == 0) {
            Intent intent = new Intent(EditPasswordActivity.this, Login.class);
            startActivity(intent);
            finish();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<Customer> call = apiService.getCustomerById(String.valueOf(customerId));

        Log.d("API Request", "URL: " + call.request().url());

        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (response.isSuccessful()) {
                    Customer customer = response.body();
                    if (customer != null) {
                        Customer = customer;
                    }
                } else {
                    // Product retrieval failed, handle the failure
                    Toast.makeText(EditPasswordActivity.this, "Failed to retrieve customer information", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                // Handle failure
                Toast.makeText(EditPasswordActivity.this, "Failed to retrieve product", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean ValidateCurrentPassword(EditText ext) {
        String val = ext.getText().toString();

        if (val.isEmpty()) {
            ext.setError("Field cannot be empty");
            return false;
        }else if (!val.equals(Customer.getPassword())) {
            ext.setError("Incorrect Password!!!!!!!");
            return false;
        } else {
            return true;
        }
    }


    private boolean ValidatePassword(EditText ext1, EditText ext2) {
        String val = ext1.getText().toString();
        String valConfirm = ext2.getText().toString();

        if (val.isEmpty()) {
            ext1.setError("Field cannot be empty!");
            return false;
        }
        if (valConfirm.isEmpty()) {
            ext2.setError("Field cannot be empty!");
            return false;
        }
        if (!valConfirm.isEmpty() && !valConfirm.equals(val)){
            ext2.setError("Passwords do not match!");
            return false;
        }
        else {
            return true;
        }

    }

    public void UpdatePassword(View view) {
        if (!ValidateCurrentPassword(etxtCurrent) || !ValidatePassword(etxtNew, etxtConfirm) ) {
            return;
        }

        Customer.setPassword(etxtNew.getText().toString());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<Customer> call = apiService.update(Customer, String.valueOf(Customer.getId()));
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(EditPasswordActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Product retrieval failed, handle the failure
                    Toast.makeText(EditPasswordActivity.this, "Failed to update information!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                // Handle failure
                Toast.makeText(EditPasswordActivity.this, "Failed to update information!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}