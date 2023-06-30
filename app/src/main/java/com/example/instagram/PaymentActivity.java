package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;


import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentActivity extends AppCompatActivity {
    private RecyclerView recyclerViewProducts;
    private TextView textViewTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
//
//        // Create a TrustManager that trusts all certificates
//        TrustManager[] trustAllCerts = new TrustManager[] {
//                new X509TrustManager() {
//                    @Override
//                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
//
//                    @Override
//                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
//
//                    @Override
//                    public X509Certificate[] getAcceptedIssuers() {
//                        return new X509Certificate[0];
//                    }
//                }
//        };
//
//    // Create an SSLContext and initialize it with the TrustManager
//        SSLContext sslContext;
//        try {
//            sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, trustAllCerts, new SecureRandom());
//        } catch (NoSuchAlgorithmException | KeyManagementException e) {
//            e.printStackTrace();
//            // Handle SSLContext initialization error
//            return;
//        }
//
//    // Set the custom SSLContext to be used by the HttpsURLConnection or networking library
//        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
//
//        // Create OkHttpClient with certificate pinning
//        OkHttpClient client = new OkHttpClient.Builder()
//                .certificatePinner(new CertificatePinner.Builder()
//                        .add("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/", "sha256/abcdefghijklmnopqrstuvwxyz1234567890=")
//                        .build())
//                .build();


        // Retrofit initialization
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of the API service
        ApiService apiService = retrofit.create(ApiService.class);
        // Call the API endpoint
        Call<List<Product>> call = apiService.getAllProduct();

        // Execute the API call asynchronously
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> products = response.body();
                    // Process the retrieved products here
                    // Create a LinearLayoutManager with the desired orientation
                    LinearLayoutManager layoutManager = new LinearLayoutManager(PaymentActivity.this, LinearLayoutManager.VERTICAL, false);
                    recyclerViewProducts.setLayoutManager(layoutManager);

                    // Set the adapter for the RecyclerView
                    ProductAdapter adapter = new ProductAdapter(PaymentActivity.this, products);
                    recyclerViewProducts.setAdapter(adapter);

                    // Calculate the total price
                    double totalPrice = calculateTotalPrice(products);

                    // Set the total price in the TextView
                    textViewTotalPrice.setText("Total Price: $" + totalPrice);
                } else {
                    // Handle error response
                    Toast.makeText(PaymentActivity.this, "Failed to retrieve products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                // Handle network errors or API call failures
                Toast.makeText(PaymentActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                Toast.makeText(PaymentActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private double calculateTotalPrice(List<Product> productList) {
        float totalPrice = 0.0f;

        // Iterate through the product list and sum up the prices
        for (Product product : productList) {
            totalPrice += product.getPrice();
        }

        return totalPrice;
    }
}