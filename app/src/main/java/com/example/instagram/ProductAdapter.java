package com.example.instagram;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.example.instagram.model.Order_Item;
import com.example.instagram.model.Product;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;
    private List<Cart_items> items;

    private int customerId;
    public ProductAdapter(Context context, List<Product> productList, List<Cart_items> items, int customerId) {
        this.context = context;
        this.productList = productList;
        this.items = items;
        this.customerId = customerId;
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
        Cart_items item = items.get(position);
        for(Product product : productList) {
            if(product.getId() == item.getProductID() && item.getCustomerId()==customerId) {
                holder.textViewName.setText(product.getName());
                holder.textViewPrice.setText("$" + product.getPrice());
                holder.textViewQuantity.setText("x" + item.getQuantity());
//                holder.imageViewProduct.setImageURI(Uri.parse(product.getImage()));
                Glide.with(context).load(Uri.parse(product.getImage())).into(holder.imageViewProduct);
            }
        }
        // Load the image URI into imageViewProduct using Glide
        /*Glide.with(holder.itemView.getContext())
                .load(product.getImage())
                .into(holder.imageViewProduct);*/
    }

    @Override
    public int getItemCount() {
        int i=0;
        for (Cart_items item : items) {
            if (item.getCustomerId() == customerId) {
                for (Product product : productList) {
                    if (item.getProductID() == product.getId()) {
                            ++i;
                        }
                    }
                }


            }
        return i;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewPrice;
        TextView textViewQuantity;
        ImageView imageViewProduct;

        public ProductViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewProductName);
            textViewPrice = itemView.findViewById(R.id.textViewProductPrice);
            textViewQuantity = itemView.findViewById(R.id.textViewProductQuantity);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
        }
    }
}
