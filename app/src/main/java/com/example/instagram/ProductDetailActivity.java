package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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
    int testProduct_id = 1;
    // test get product id to show product detail
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ImageView imageProductDetail = findViewById(R.id.imageView7);

//        String imageUrl = "https://i.pinimg.com/564x/c9/a2/7b/c9a27b13884779ab61cc23756ea2b05f.jpg";
//        Picasso.get().load(imageUrl).into(imageProductDetail);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<Product> call = apiService.getProductById(testProduct_id);

        Log.d("API Request", "URL: " + call.request().url());

        // cho nay ne thuc ôiiiiii
        call.enqueue(new Callback<Product>() {

            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Product product = response.body();
                    if (product != null) {
                        // Product retrieval successful, perform necessary actions
                        // ...
                        Toast.makeText(ProductDetailActivity.this, "Helllo huhuh", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Product retrieval failed, handle the failure
                    // ...
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                // Handle failure
                // ...
            }
        });

// cho nay ne thuc ôiiiiii



    }
}