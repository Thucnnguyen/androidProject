package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.model.Order;
import com.example.instagram.model.OrderItem;

import java.util.List;



public class OrderItemAdapter  extends RecyclerView.Adapter<OrderItemAdapter.OrderItemListViewHolder>{
    private Context mContext;
    private List<OrderItem> mWordList;
    public OrderItemAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<OrderItem> list) {
        this.mWordList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderItemAdapter.OrderItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderdetai_item, parent, false);
        return new OrderItemAdapter.OrderItemListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemAdapter.OrderItemListViewHolder holder, int position) {
        OrderItem item = mWordList.get(position);
        if (item == null) {
            return;
        }
//
        Log.d("itemAtDetail",item.toString());
        holder.imgItem.setImageResource(R.drawable.orderitem);
        holder.orderDetailId.setText("Order detail: "+String.valueOf(item.getId()));
        holder.product_id.setText("Product Id: " + String.valueOf(item.getProductId()));
        holder.quantity.setText("Quantity: " +String.valueOf(item.getQuantity()));
        holder.price.setText( "Price: "+ String.valueOf(item.getPrice()));
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, OrderItemActivity.class);
//                intent.putExtra("orderId", item.getId());
//                mContext.startActivity(intent);
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



    public class OrderItemListViewHolder extends RecyclerView.ViewHolder{
        private CardView card;
        private ImageView imgItem;
        private TextView orderDetailId;
        private TextView product_id;
        private TextView quantity;
        private TextView price;

//        private TextView

        public OrderItemListViewHolder(@NonNull View itemView){
            super(itemView);

            imgItem = itemView.findViewById(R.id.img_user);
            orderDetailId = itemView.findViewById(R.id.orderDetailId);
            product_id = itemView.findViewById(R.id.product_id);
            quantity = itemView.findViewById(R.id.quantity);
            price = itemView.findViewById(R.id.price);
            card =itemView.findViewById(R.id.card_view);
        }
    }

}
