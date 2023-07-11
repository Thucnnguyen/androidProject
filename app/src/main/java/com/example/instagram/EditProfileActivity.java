package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.concurrent.atomic.AtomicBoolean;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProfileActivity extends AppCompatActivity {

    private Button btnReturn, btnConfirm;
    private String currentEmail;
    private EditText etxtName, etxtEmail, etxtAddress, etxtPhone, etxtPassword;
    private int customerId;

    private boolean check ;
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


        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE);
        customerId = sharedPreferences.getInt("customerId",0);
        if (customerId == 0) {
            Intent intent = new Intent(EditProfileActivity.this, Login.class);
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
                        BindingData(customer);
                    }
                } else {
                    // Product retrieval failed, handle the failure
                    Toast.makeText(EditProfileActivity.this, "Failed to retrieve customer information", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                // Handle failure
                Toast.makeText(EditProfileActivity.this, "Failed to retrieve product", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void validateEmail(EmailValidationCallback callback) {
        String val = etxtEmail.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        AtomicBoolean emailExists = new AtomicBoolean(false);

        if (val.isEmpty()) {
            etxtEmail.setError("Field cannot be empty");
            callback.onEmailValidated(false);
            return;
        } else if (!val.matches(emailPattern)) {
            etxtEmail.setError("Email is invalid");
            callback.onEmailValidated(false);
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<Customer>> call = apiService.getCustomer();

        call.enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                if (response.isSuccessful()) {
                    List<Customer> customers = response.body();
                    if (customers != null && customers.size() > 0) {
                        for (Customer c : customers) {
                            if (c.getEmail().trim().equals(val.trim()) && !c.getEmail().trim().equals(currentEmail)) {
                                etxtEmail.setError("Email already exists");
                                emailExists.set(true);
                                break;
                            }
                        }
                    }
                }
                callback.onEmailValidated(emailExists.get());
            }

            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {
                // Handle failure if needed
                callback.onEmailValidated(false);
            }
        });

        etxtEmail.setError(null);
    }

    private void checkFalse(){
        check = false;
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
        currentEmail = cus.getEmail();
    }

    public void UpdateCustomer(View view) {
        if (!ValidateName() || !ValidatePhone() || !ValidatePassword() || !ValidateAddress()) {
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
        //Toasty.error(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();

        Call<Customer> call = apiService.update(cus, String.valueOf(customerId));
        Intent intent = new Intent(this, Login.class);
        validateEmail(new EmailValidationCallback() {
            @Override
            public void onEmailValidated(boolean isEmailValid) {
                if (!isEmailValid) {
                    // Email is valid, proceed with the registration
                    // Your registration logic goes here
                    call.enqueue(new Callback<Customer>() {
                        @Override
                        public void onResponse(Call<Customer> call, Response<Customer> response) {
                            if (response.isSuccessful()) {
                                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                intent.putExtra("message", "Update Success!!");
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Customer> call, Throwable t) {
                            Toasty.error(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Email is invalid or already exists, display an error message or take appropriate action
                }
            }
        });
    }
}