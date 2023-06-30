package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;

import com.squareup.picasso.Picasso;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {

    private Button btnReturn, btnLogout, btnEdit;
    private TextView txtName, txtEmail, txtAddress, txtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_profile);

        btnReturn = (Button) findViewById(R.id.profile_return);
        btnEdit = (Button) findViewById(R.id.edit_profile);
        btnLogout = (Button) findViewById(R.id.logout_button);

        String customerId = "1";

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
                    Toast.makeText(ProfileActivity.this, "Failed to retrieve product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                // Handle failure
                Toast.makeText(ProfileActivity.this, "Failed to retrieve customer information", Toast.LENGTH_SHORT).show();
            }
        });


        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProductList.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("customer", (Serializable) customer);
//                intent.putExtras(bundle);
                startActivity(intent);
//                onBackPressed();
                finish();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("customer", (Serializable) customer);
//                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, Login.class);
                startActivity(intent);
                finish();
        }
        });
    }

    private void BindingData(Customer cus){
        txtName = (TextView) findViewById(R.id.profile_name);
        txtEmail = (TextView) findViewById(R.id.profile_email);
        txtAddress = (TextView) findViewById(R.id.profile_address);
        txtPhone = (TextView) findViewById(R.id.profile_phone);

        txtName.setText(cus.getName());
        txtEmail.setText(cus.getEmail());
        txtPhone.setText(cus.getPhone());
        txtAddress.setText(cus.getAddress());
    }


}