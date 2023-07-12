package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.PopupMenu;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.instagram.model.Customer;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {
    private GoogleSignInClient googleSignInClient;
    private Button btnReturn, btnLogout, btnEdit;
    private TextView txtName, txtEmail, txtAddress, txtPhone;
    private int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_profile);

        btnReturn = findViewById(R.id.profile_return);
        btnEdit = findViewById(R.id.edit_profile);
        btnLogout = findViewById(R.id.logout_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE);
        customerId = sharedPreferences.getInt("customerId", 0);

        if (customerId == 0) {
            Intent intent = new Intent(ProfileActivity.this, Login.class);
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
                    Log.d("A", customer.getName());
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
                logout();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.profileNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.person);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    Intent intent = new Intent(ProfileActivity.this, ProductList.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.history) {
                    // Handle History item click
                    return true;
                } else if (item.getItemId() == R.id.person) {

                    return true;
                } else if (item.getItemId() == R.id.cart) {
                    // Handle Cart item click
//                    Intent intent = new Intent(ProductList.this, activity_cartlist.class);
//                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.profile_menu, popupMenu.getMenu());

        // Set item click listener for the menu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item clicks here
                if (item.getItemId() == R.id.profile_edit_btn) {
                    Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.profile_logout_btn) {
//                    Intent intent = new Intent(ProfileActivity.this, Login.class);
//                    startActivity(intent);
//                    finish();
                    logout();
                    return true;
                } else return false;
            }
        });

        // Show the popup menu
        popupMenu.show();
    }


    private void BindingData(Customer cus) {
        txtName = findViewById(R.id.profile_name);
        txtEmail = findViewById(R.id.profile_email);
        txtAddress = findViewById(R.id.profile_address);
        txtPhone = findViewById(R.id.profile_phone);

        txtName.setText(cus.getName());
        txtEmail.setText(cus.getEmail());
        txtPhone.setText(cus.getPhone());
        txtAddress.setText(cus.getAddress());
    }

    private void logout() {
        // Sign out from Google Sign-In
        googleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Clear shared preferences and navigate to Login activity
                SharedPreferences sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("isLogin", false).apply();

                Intent intent = new Intent(ProfileActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}