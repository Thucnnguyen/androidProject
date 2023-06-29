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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProfileActivity extends AppCompatActivity {

    private Button btnReturn;

    private EditText etxtName, etxtEmail, etxtAddress, etxtPhone, etxtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);

        btnReturn = (Button) findViewById(R.id.edit_profile_return);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        int customerId = getIntent().getIntExtra("id", 1);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<Customer> call = apiService.getCustomerById(customerId);

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
                    Toast.makeText(EditProfileActivity.this, "Failed to retrieve product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                // Handle failure
                Toast.makeText(EditProfileActivity.this, "Failed to retrieve product", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void BindingData(Customer cus){
        etxtName = (EditText) findViewById(R.id.edit_name);
        etxtEmail = (EditText) findViewById(R.id.edit_email);
        etxtAddress = (EditText) findViewById(R.id.edit_address);
        etxtPhone = (EditText) findViewById(R.id.edit_phone);
        etxtPassword = (EditText) findViewById(R.id.edit_password);

        etxtName.setText(cus.getName());
        etxtEmail.setText(cus.getEmail());
        etxtAddress.setText(cus.getPhone());
        etxtPhone.setText(cus.getAddress());
        etxtPassword.setText(cus.getPassword());
    }
}