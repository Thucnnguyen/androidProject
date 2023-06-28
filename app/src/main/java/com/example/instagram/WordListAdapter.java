package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

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


        public WordListViewHolder(@NonNull View itemView){
            super(itemView);

            imgItem = itemView.findViewById(R.id.imageView6);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            card =itemView.findViewById(R.id.card_view);
        }
    }

}
