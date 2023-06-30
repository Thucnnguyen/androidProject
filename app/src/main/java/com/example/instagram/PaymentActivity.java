package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
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
    private TextView textViewTotalPrice, textViewCustName, textViewCustPhone, textviewCustAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        textViewCustName = findViewById(R.id.textViewUserName);
        textViewCustPhone = findViewById(R.id.textViewPhone);
        textviewCustAddr = findViewById(R.id.textViewAddr);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);


        // Retrofit initialization
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of the API service
        ApiService apiService = retrofit.create(ApiService.class);
        // Call the API endpoint
        Call call = apiService.getCartItems();

        // Execute the API call asynchronously
        call.enqueue(new Callback<Cart_items[]>() {
            @Override
            public void onResponse(Call<Cart_items[]> call, Response<Cart_items[]> response) {
                if (response.isSuccessful()) {

                    Cart_items[] items = response.body();
                    List<Product> products = new ArrayList<Product>() ;
                    for(int i = 0; i < items.length;i++) {
                        Call productCall = apiService.getProductById(items[i].getProductID());

                        productCall.enqueue(new Callback<Product>() {
                            @Override
                            public void onResponse(Call<Product> call, Response<Product> response) {
                                if (response.isSuccessful()) {
                                    Product product = response.body();
                                    products.add(product);
                                    Toast.makeText(PaymentActivity.this,product.getName(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Product> call, Throwable t) {
                                Log.d("CREATION", "onFailure: " + t.getMessage());
                                Toast.makeText(PaymentActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    // Process the retrieved products here
                    // Create a LinearLayoutManager with the desired orientation
                    LinearLayoutManager layoutManager = new LinearLayoutManager(PaymentActivity.this, LinearLayoutManager.VERTICAL, false);
                    recyclerViewProducts.setLayoutManager(layoutManager);

                    // Set the adapter for the RecyclerView
                    ProductAdapter adapter = new ProductAdapter(PaymentActivity.this, products, items);
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
            public void onFailure(Call<Cart_items[]> call, Throwable t) {
                // Handle network errors or API call failures
                Toast.makeText(PaymentActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                Toast.makeText(PaymentActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
        //Get Customer Info
        String customerId = "1";

        call = apiService.getCustomerById(customerId);

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
                    Toast.makeText(PaymentActivity.this, "Failed to retrieve customer information", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                // Handle failure
                Toast.makeText(PaymentActivity.this, "Failed to retrieve product", Toast.LENGTH_SHORT).show();
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

    private void BindingData(Customer cus){

        textViewCustName.setText(cus.getName());
        textviewCustAddr.setText(cus.getAddress());
        textViewCustPhone.setText(cus.getPhone());
    }


}