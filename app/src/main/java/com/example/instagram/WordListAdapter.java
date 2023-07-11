package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.model.Customer;
import com.example.instagram.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordListViewHolder>{

    private Context mContext;
    private List<Product> mWordList;

    public WordListAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<Product> list) {
        this.mWordList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WordListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wordlist_item, parent, false);
        return new WordListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordListViewHolder holder, int position) {
        Product item = mWordList.get(position);
        if (item == null) {
            return;
        }

        Picasso.get().load(item.getImage()).into(holder.imgItem);
        holder.name.setText(item.getName());
        holder.price.setText(String.valueOf(item.getPrice()));
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyApp", Context.MODE_PRIVATE);
                int cusId = sharedPreferences.getInt("customerId", -1);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ApiService apiService = retrofit.create(ApiService.class);
                Call<List<Cart_items>> call = apiService.getCartItems();
                call.enqueue(new Callback<List<Cart_items>>() {
                    @Override
                    public void onResponse(Call<List<Cart_items>> call, Response<List<Cart_items>> response) {
                        if(response.isSuccessful()){
                            List<Cart_items> items = response.body();
                            Cart_items searchCart = null;
                            if(items!=null){
                                for (Cart_items c: items
                                     ) {
                                    if(c.getProductID() == item.getId() || c.getCustomerId() == cusId){
                                        searchCart = c;
                                    }
                                }
                            }
                            if(searchCart!=null) {
                                searchCart.setQuantity(searchCart.getQuantity() + 1);
                                Call<ResponseBody> updateCart = apiService.updateCartItems(item.getId(), searchCart);
                                updateCart.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            Toasty.info(mContext, "Add Success", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toasty.info(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }else{
                                Call<Cart_items> add = apiService.addCartItems(new Cart_items(cusId,item.getId(),1));
                                add.enqueue(new Callback<Cart_items>() {
                                    @Override
                                    public void onResponse(Call<Cart_items> call, Response<Cart_items> response) {
                                        if(response.isSuccessful()){
                                            Toasty.info(mContext ,"Add Success", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Cart_items> call, Throwable t) {
                                        Toasty.info(mContext ,t.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Cart_items>> call, Throwable t) {
                        Toasty.info(mContext ,t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra("productId", item.getId());
                mContext.startActivity(intent);
            }
        });
    }

    protected void release(){
        mContext = null;
    }


    @Override
    public int getItemCount() {
        return mWordList != null ? mWordList.size() : 0;
    }



    public class WordListViewHolder extends RecyclerView.ViewHolder{
        private ConstraintLayout card;
        private ImageView imgItem;
        private TextView name;
        private TextView price;
        private TextView addButton;


        public WordListViewHolder(@NonNull View itemView){
            super(itemView);


            imgItem = itemView.findViewById(R.id.imageView6);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            addButton= itemView.findViewById(R.id.textView17);
            card =itemView.findViewById(R.id.card_view);
        }
    }

}
