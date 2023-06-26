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
    int testProduct_id = 2;
    // test get product id to show product detail
    ImageView imageProductDetail;
    TextView productNameTextView;
    TextView productPriceTextView;
    TextView productDescriptionTextView;
    private void getProductDetail() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<Product> call = apiService.getProductById(testProduct_id);

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
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

         imageProductDetail = findViewById(R.id.imageView7);
         productNameTextView = findViewById(R.id.textView11);
         productPriceTextView = findViewById(R.id.textView12);
         productDescriptionTextView = findViewById(R.id.textView13);
        getProductDetail();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        ApiService apiService = retrofit.create(ApiService.class);
//
//        Call<Product> call = apiService.getProductById(testProduct_id);
//
//        Log.d("API Request", "URL: " + call.request().url());
//
//        call.enqueue(new Callback<Product>() {
//            @Override
//            public void onResponse(Call<Product> call, Response<Product> response) {
//                if (response.isSuccessful()) {
//                    Product product = response.body();
//                    if (product != null) {
//                        // Set the product data in the views
//
//                        productNameTextView.setText(product.getName());
//                        productPriceTextView.setText(String.valueOf(product.getPrice()));
//                        productDescriptionTextView.setText(product.getDesciption());
//                        Picasso.get().load(product.getImage()).into(imageProductDetail);
//                    }
//                } else {
//                    // Product retrieval failed, handle the failure
//                    Toast.makeText(ProductDetailActivity.this, "Failed to retrieve product", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Product> call, Throwable t) {
//                // Handle failure
//                Toast.makeText(ProductDetailActivity.this, "Failed to retrieve product", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}