package com.example.instagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
//import com.bumptech.glide.Glide;
import com.example.instagram.model.Product;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;
    private Cart_items[] items;

    public ProductAdapter(Context context, List<Product> productList, Cart_items[] items) {
        this.context = context;
        this.productList = productList;
        this.items = items;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Cart_items item = items[position];
        Product product = productList.get(item.getProductID());
        holder.textViewName.setText(product.getName());
        holder.textViewPrice.setText("$" + product.getPrice());
        holder.textViewPrice.setText("x" + item.getQuantity());
        // Load the image URI into imageViewProduct using Glide
        /*Glide.with(holder.itemView.getContext())
                .load(product.getImage())
                .into(holder.imageViewProduct);*/
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewPrice;
        ImageView imageViewProduct;

        public ProductViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewProductName);
            textViewPrice = itemView.findViewById(R.id.textViewProductPrice);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
        }
    }
}
