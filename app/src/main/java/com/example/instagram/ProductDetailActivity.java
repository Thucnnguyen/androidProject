package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductDetailActivity extends AppCompatActivity {

    // test get product id to show product detail
    int productId;
    // test get product id to show product detail
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ImageView imageProductDetail = findViewById(R.id.imageView7);
        TextView productNameTextView = findViewById(R.id.textView11);
        TextView productPriceTextView = findViewById(R.id.textView12);
        TextView productDescriptionTextView = findViewById(R.id.textView13);
        productId =getIntent().getIntExtra("productId", -1);
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
                        Picasso.get().load(product.getImage()).into(imageProductDetail);
                        productNameTextView.setText(product.getName());
                        productPriceTextView.setText("$ " + product.getPrice());
                        productDescriptionTextView.setText(product.getDesciption());
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
    }
}