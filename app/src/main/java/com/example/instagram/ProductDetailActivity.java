package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ImageView imageProductDetail = findViewById(R.id.imageView7);

        String imageUrl = "https://i.pinimg.com/564x/c9/a2/7b/c9a27b13884779ab61cc23756ea2b05f.jpg";
        Picasso.get().load(imageUrl).into(imageProductDetail);
//        Picasso.get()
//                .load(imageUrl)
//                .fit()
//                .placeholder(R.drawable.placeholder_image) // Replace with your placeholder image resource
//                .into(imageProductDetail);
    }
}