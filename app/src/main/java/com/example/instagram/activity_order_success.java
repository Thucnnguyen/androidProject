package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class activity_order_success extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        // Display the success message with the order ID
        TextView textViewOrderSuccess = findViewById(R.id.textViewOrderSuccess);
        textViewOrderSuccess.setText("Order placed successfully!");

        Button buttonBackToHome = findViewById(R.id.buttonBackToHome);
        buttonBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate back to the home page
                Intent intent = new Intent(activity_order_success.this, ProductList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}