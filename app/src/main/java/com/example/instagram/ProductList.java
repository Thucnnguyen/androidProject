package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.instagram.model.Product;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductList extends AppCompatActivity {

    private RecyclerView rvItem;
    private TextView searchButton;
    private WordListAdapter adapter;
    private ConstraintLayout top;
    private ConstraintLayout bottom;
    private ConstraintLayout all;
    private TextView helloText;
    private EditText searchText;
    private GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        if (!name.isEmpty()) {
            helloText = findViewById(R.id.hello_text);
            helloText.setText(helloText.getText()+" "+name);
        }
        searchText = findViewById(R.id.editTextText2);
        searchButton = findViewById(R.id.searchButton);
        rvItem = findViewById(R.id.product_recycleView);
        adapter = new WordListAdapter(this);

        GridLayoutManager grid = new GridLayoutManager(this, 3);
        rvItem.setLayoutManager(grid);

        getListItem();
        rvItem.setAdapter(adapter);


        Button button = (Button) findViewById(R.id.logout_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        top = findViewById(R.id.top_clothes);
        bottom = findViewById(R.id.bottom_clothes);
        all = findViewById(R.id.all_clothes);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListItem(1);
            }
        });

        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListItem(2);
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListItem();
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListItem(searchText.getText().toString());
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {

                    return true;
                } else if (item.getItemId() == R.id.history) {
                    // Handle History item click
                    return true;
                } else if (item.getItemId() == R.id.person) {
                    // Handle Profile item click
                    return true;
                } else if (item.getItemId() == R.id.cart) {
                    // Handle Cart item click
                    Intent intent = new Intent(ProductList.this, activity_cartlist.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    private void getListItem() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<Product>> call = apiService.getAllProduct();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> products = response.body();
                    if (products != null) {
                        adapter.setData(products);  // Update the adapter's data
                        adapter.notifyDataSetChanged();
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });
    }
    private void getListItem(int categoryId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<Product>> call = apiService.getAllProduct();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> products = response.body();
                    List<Product> result = new ArrayList<>();

                    if (products != null) {
                        for (Product p : products
                             ) {
                            if(p.getCategoryId() == categoryId){

                                result.add(p);
                            }
                        }
                        adapter.setData(result);  // Update the adapter's data
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    // Product retrieval failed, handle the failure
                    // ...
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });
    }
    private void getListItem(String searchText) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<Product>> call = apiService.searchProductByName(searchText);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> products = response.body();


                    if (products != null) {
                        adapter.setData(products);  // Update the adapter's data
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    // Product retrieval failed, handle the failure
                    // ...
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });
    }
    private void logout() {
        // Sign out from Google Sign-In
        googleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Clear shared preferences and navigate to Login activity
                SharedPreferences sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("isLogin", false).apply();

                Intent intent = new Intent(ProductList.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}