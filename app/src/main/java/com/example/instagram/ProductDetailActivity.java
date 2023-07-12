package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductDetailActivity extends AppCompatActivity {

    // test get product id to show product detail
    int productId;
    // test get product id to show product detail

    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Button addToCart = findViewById(R.id.button2);
        ImageView imageProductDetail = findViewById(R.id.imageView7);
        TextView productNameTextView = findViewById(R.id.textView11);
        TextView productPriceTextView = findViewById(R.id.textView12);
        TextView productDescriptionTextView = findViewById(R.id.textView13);
        TextView quantityTextView = findViewById(R.id.quantity);
        ImageView plusImageView = findViewById(R.id.plusQuantity);
        ImageView minusImageView = findViewById(R.id.imageView8);

        // Set click listener for the plus ImageView
        plusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increase the quantity by 1
                quantity++;

                // Update the quantity TextView
                quantityTextView.setText(String.valueOf(quantity));
            }
        });

        // Set click listener for the minus ImageView
        minusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ensure the quantity is greater than 1 before decreasing
                if (quantity > 1) {
                    // Decrease the quantity by 1
                    quantity--;

                    // Update the quantity TextView
                    quantityTextView.setText(String.valueOf(quantity));
                }
            }
        });


        productId = getIntent().getIntExtra("productId", -1);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<Product> call = apiService.getProductById(productId);

        Log.d("API Request", "URL: " + call.request().url());

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Product product = response.body();
                    if (product != null) {
                        // Set the product data in the views
                        productNameTextView.setText(product.getName());
                        productPriceTextView.setText(String.valueOf(product.getPrice()));
                        productDescriptionTextView.setText(product.getDesciption());
                        Picasso.get().load(product.getImage()).into(imageProductDetail);
                    }
                } else {
                    // Product retrieval failed, handle the failure
                    Toast.makeText(ProductDetailActivity.this, "Failed to retrieve product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                // Handle failure
                Toast.makeText(ProductDetailActivity.this, "Failed to retrieve product", Toast.LENGTH_SHORT).show();
            }
        });
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE);
                int cusId = sharedPreferences.getInt("customerId", -1);
                Intent intent = new Intent(ProductDetailActivity.this, activity_cartlist.class);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ApiService apiService = retrofit.create(ApiService.class);
                Call<List<Cart_items>> call = apiService.getCartItems(cusId);
                call.enqueue(new Callback<List<Cart_items>>() {
                    @Override
                    public void onResponse(Call<List<Cart_items>> call, Response<List<Cart_items>> response) {
                        if (response.isSuccessful()) {
                            List<Cart_items> items = response.body();
                            Cart_items searchCart = null;
                            if (items != null) {
                                for (Cart_items c : items
                                ) {
                                    if (c.getProductID() == productId || c.getCustomerId() == cusId) {
                                        searchCart = c;
                                    }
                                }
                            }
                            if (searchCart != null) {
                                searchCart.setQuantity(searchCart.getQuantity() + 1);
                                Call<ResponseBody> updateCart = apiService.updateCartItems(productId, searchCart);
                                updateCart.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            Toasty.info(ProductDetailActivity.this, "Add Success", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toasty.info(ProductDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                            } else {
                                Call<Cart_items> add = apiService.addCartItems(new Cart_items(cusId, productId, quantity));
                                add.enqueue(new Callback<Cart_items>() {
                                    @Override
                                    public void onResponse(Call<Cart_items> call, Response<Cart_items> response) {
                                        if (response.isSuccessful()) {
                                            Toasty.info(ProductDetailActivity.this, "Add Success", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Cart_items> call, Throwable t) {
                                        Toasty.info(ProductDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Cart_items>> call, Throwable t) {
                        Toasty.info(ProductDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
//                startActivity(intent);
//                activity_cartlist a = new activity_cartlist();
////                Product prod = new Product();
//                a.AddToCart(productId, quantity);
            }
        });
    }

}
