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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderListAdapter  extends RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder>{
    private Context mContext;
    private List<Order> mWordList;
    public OrderListAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<Order> list) {
        this.mWordList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderListAdapter.OrderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderlist_item, parent, false);
        return new OrderListAdapter.OrderListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.OrderListViewHolder holder, int position) {
        Order item = mWordList.get(position);
        if (item == null) {
            return;
        }
        Log.d("stau", item.toString());
        holder.imgItem.setImageResource(R.drawable.wishlist_product_list_order_cart_icon_225175);
        holder.orderId.setText("OrderId: " + String.valueOf(item.getId()));
        holder.oderStatus.setText("Order Status: " + item.getOderStatus());
        holder.orderDate.setText( "order_date: "+getFormattedDate(item.getOrderDate()));
//        holder.shipDate.setText("ship_date: "+getFormattedDate(item.getShipDate()));
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderItemActivity.class);
                intent.putExtra("orderId", item.getId());
                mContext.startActivity(intent);

                Log.d("testORderid ", String.valueOf(item.getId()));
            }
        });
    }

    protected void release(){
        mContext = null;
    }

    private String getFormattedDate(Long timestamp) {
        if (timestamp != null) {
            Date date = new Date(timestamp * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdf.format(date);
        } else {
            return "";
        }
    }
    @Override
    public int getItemCount() {
        return mWordList != null ? mWordList.size() : 0;
    }



    public class OrderListViewHolder extends RecyclerView.ViewHolder{
        private CardView card;
        private ImageView imgItem;
        private TextView orderId;
        private TextView oderStatus;
        private TextView orderDate;
//        private TextView shipDate;

//        private TextView

        public OrderListViewHolder(@NonNull View itemView){
            super(itemView);

            imgItem = itemView.findViewById(R.id.img_user);
            orderId = itemView.findViewById(R.id.orderId);
            oderStatus = itemView.findViewById(R.id.oderStatus);
            orderDate = itemView.findViewById(R.id.orderDate);
//            shipDate = itemView.findViewById(R.id.shipDate);
            card =itemView.findViewById(R.id.card_view);
        }
    }

}
