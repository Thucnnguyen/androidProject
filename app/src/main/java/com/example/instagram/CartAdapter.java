package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.model.Product;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    public List<Product> products;
    public ArrayList<Cart_items> cartItemList;
    public activity_cartlist context;



    public CartAdapter(activity_cartlist context, ArrayList<Cart_items> cartItemList, List<Product> products) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.products = products;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.row_cart, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;

    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = new Product();

        Cart_items cart = cartItemList.get(position);

        for (Product p :products){
            if (p.getId() == cart.getProductID())
                product = p;
        }
        new DownloadImageTask(holder.ivProduct).execute(product.getImage());
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText("Price : " + product.getPrice() * cart.getQuantity() + "$");
        holder.tvQuantity.setText(cart.getQuantity()+"");




        holder.btnAddOne.setOnClickListener(v -> {

            context.AddToCart(cart, 1);
        });
        holder.btnAddOne.setOnLongClickListener(v -> {
            context.AddToCart(cart, 10);
            return true;
        });

        holder.btnMinusOne.setOnClickListener(v -> {
            context.RemoveFromCart(cart, 1);
        });

        holder.btnMinusOne.setOnLongClickListener(v -> {
            context.RemoveFromCart(cart, 10);
            return true;
        });
        holder.remove.setOnClickListener(v -> {
            context.RemoveFromCart(cart, 999);

        });

    }


    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }


        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvName, tvPrice, tvQuantity;
        ImageButton btnAddOne, btnMinusOne,remove,back;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivProduct);

            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPriceQuantity);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);

            btnAddOne = itemView.findViewById(R.id.btnAddOne);
            btnMinusOne = itemView.findViewById(R.id.btnMinusOne);
            remove = itemView.findViewById(R.id.remove);


        }

    }


}