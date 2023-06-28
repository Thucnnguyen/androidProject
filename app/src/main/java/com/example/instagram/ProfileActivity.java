package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;

public class ProfileActivity extends AppCompatActivity {

    private Button btnReturn, btnLogout;
    private TextView txtName, txtEmail, txtAddress, txtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_profile);

        Customer customer = new Customer("User 123", "User1@gmail.com", "010101010101", "8113 Creekbend Dr Houston, Texas(TX), 77071", "String password", "String id");

        BindingData(customer);

        btnReturn = (Button) findViewById(R.id.profile_return);
        btnLogout = (Button) findViewById(R.id.logout_button);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProductList.class);
                startActivity(intent);
                finish();
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