package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.TextView;
import java.util.ArrayList;


import android.os.Bundle;

public class PaymentActivity extends AppCompatActivity {
    private RecyclerView recyclerViewProducts;
    private TextView textViewTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);

        // Get the list of products from your data source
        ArrayList<Product> productList = getProductList();
        // Create a LinearLayoutManager with the desired orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewProducts.setLayoutManager(layoutManager);

// Set the adapter for the RecyclerView
        ProductAdapter adapter = new ProductAdapter(this, productList);
        recyclerViewProducts.setAdapter(adapter);

        // Calculate the total price
        double totalPrice = calculateTotalPrice(productList);

        // Set the total price in the TextView
        textViewTotalPrice.setText("Total Price: $" + totalPrice);
    }

    // Example method to retrieve the list of products
    private ArrayList<Product> getProductList() {
        // Replace this with your actual logic to fetch the list of products
        ArrayList<Product> productList = new ArrayList<>();

        // Add dummy products for demonstration
        productList.add(new Product("Product 1",1, 10.99f,"as", String.valueOf(R.drawable.prod),1));
        productList.add(new Product("Product 2",1, 19.99f,"as", String.valueOf(R.drawable.prod),1));
        productList.add(new Product("Product 3",1, 5.99f,"as", String.valueOf(R.drawable.prod),1));
        return productList;
    }

    // Example method to calculate the total price
    private double calculateTotalPrice(ArrayList<Product> productList) {
        double totalPrice = 0.0;

        // Iterate through the product list and sum up the prices
        for (Product product : productList) {
            totalPrice += product.getPrice();
        }

        return totalPrice;
    }
}