package com.example.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.example.instagram.model.Customer;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProfileActivity extends AppCompatActivity {

    private Button btnReturn, btnConfirm;

    private EditText etxtName, etxtEmail, etxtAddress, etxtPhone, etxtPassword;
    private String customerId;

    private static boolean check = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);

        etxtName = (EditText) findViewById(R.id.edit_name);
        etxtEmail = (EditText) findViewById(R.id.edit_email);
        etxtAddress = (EditText) findViewById(R.id.edit_address);
        etxtPhone = (EditText) findViewById(R.id.edit_phone);
        etxtPassword = (EditText) findViewById(R.id.edit_password);

        btnReturn = (Button) findViewById(R.id.edit_profile_return);
        btnConfirm = (Button) findViewById(R.id.edit_confirm);

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
                UpdateCustomer(v);
            }
        });


        customerId = "1";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

//        Call<Customer> call = apiService.getCustomerById(customerId);
//
//        Log.d("API Request", "URL: " + call.request().url());
//
//        call.enqueue(new Callback<Customer>() {
//            @Override
//            public void onResponse(Call<Customer> call, Response<Customer> response) {
//                if (response.isSuccessful()) {
//                    Customer customer = response.body();
//                    if (customer != null) {
//                        BindingData(customer);
//                    }
//                } else {
//                    // Product retrieval failed, handle the failure
//                    Toast.makeText(EditProfileActivity.this, "Failed to retrieve customer information", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Customer> call, Throwable t) {
//                // Handle failure
//                Toast.makeText(EditProfileActivity.this, "Failed to retrieve product", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private boolean ValidateName() {
        String val = etxtName.getText().toString();
        if (val.isEmpty()) {
            etxtName.setError("Field cannot be empty");
            return false;
        } else {
            return true;
        }
    }

    private boolean ValidateEmail() {
        String val = etxtEmail.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        MutableLiveData<Boolean> check = new MutableLiveData<Boolean>();
        if (val.isEmpty()) {
            etxtEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            etxtEmail.setError("Email is invalid");
            return false;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<Customer>> call = apiService.getCustomer();
        check.setValue(true);
        call.enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                if (response.isSuccessful()) {
                    List<Customer> customers = response.body();
                    if (customers != null && customers.size() > 0) {
                        for (Customer c : customers
                        ) {
                            if (c.getEmail().trim().equals(val.trim())) {
                                if(!c.getId().trim().equals(customerId.trim()))
                                {
                                etxtEmail.setError("Email is exist");
                                    check.setValue(false);
                                break;
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {

            }
        });
        return check.getValue();
    }

    private boolean ValidatePhone() {
        String val = etxtPhone.getText().toString();
        if (val.isEmpty()) {
            etxtPhone.setError("Field cannot be empty");
            return false;
        } else {
            return true;
        }
    }

    private boolean ValidatePassword() {
        String val = etxtPassword.getText().toString();

        if (val.isEmpty()) {
            etxtPassword.setError("Field cannot be empty");
            return false;
        } else {
            return true;
        }
    }

    private boolean ValidateAddress() {
        String val = etxtAddress.getText().toString();
        if (val.isEmpty()) {
            etxtAddress.setError("Field cannot be empty");
            return false;
        } else {
            return true;
        }
    }
//
    private void BindingData(Customer cus){

        etxtName.setText(cus.getName());
        etxtEmail.setText(cus.getEmail());
        etxtAddress.setText(cus.getAddress());
        etxtPhone.setText(cus.getPhone());
        etxtPassword.setText(cus.getPassword());
    }

    public void UpdateCustomer(View view) {
        if (!ValidateName() | !ValidateEmail() | !ValidatePhone() | !ValidatePassword() | !ValidateAddress()) {
            return;
        }
        String fullName = etxtName.getText().toString();

        String email = etxtEmail.getText().toString();
        String phone = etxtPhone.getText().toString();
        String password = etxtPassword.getText().toString();
        String address = etxtAddress.getText().toString();
        Customer cus = new Customer(fullName, email, phone, address, password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

//        Call<Customer> call = apiService.update(cus, customerId);
//        Intent intent = new Intent(this, Login.class);
//        call.enqueue(new Callback<Customer>() {
//            @Override
//            public void onResponse(Call<Customer> call, Response<Customer> response) {
//                if (response.isSuccessful()) {
//                    Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
//                    intent.putExtra("message", "Update Success!!");
//                    startActivity(intent);
//                    finish();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Customer> call, Throwable t) {
//                Toasty.error(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}