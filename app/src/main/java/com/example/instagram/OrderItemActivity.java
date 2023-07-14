package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.instagram.model.Order;
import com.example.instagram.model.OrderItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderItemActivity extends AppCompatActivity {
    int orderId;
    private TextView orderIdText;

    private RecyclerView rvItem;

    private Button back;
    private OrderItemAdapter adapter;
private TextView orderStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_item);


        back = findViewById(R.id.backOrderItem);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Retrieve the orderId from the intent
        Intent intent = getIntent();
        if (intent != null) {
            orderId = intent.getIntExtra("orderId", 0);
        }

        // Use the orderId as needed
        Log.d("OrderItemActivity", "Received orderId: " + orderId);


        rvItem = findViewById(R.id.rcv_user);
        adapter = new OrderItemAdapter(this);

        GridLayoutManager grid = new GridLayoutManager(this, 2);
        rvItem.setLayoutManager(grid);

        getListItem(orderId);
        rvItem.setAdapter(adapter);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);




    }


    private void getListItem(int orderId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<OrderItem>> call = apiService.getOrderItemsByOrderId(orderId);
//        Call<List<Order>> call = apiService.getOrders();
        call.enqueue(new Callback<List<OrderItem>>() {
            @Override
            public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                if (response.isSuccessful()) {
                    List<OrderItem> products = response.body();
                    Log.d("orderDetailSize,:", String.valueOf(products.size()));
                    TextView totalPrice = findViewById(R.id.totalPrice);
                    double total = 0;
                    for (int i = 0; i <  products.size(); i++) {
                        total += products.get(i).getPrice() * products.get(i).getQuantity() - products.get(i).getDiscount();
                    }
                    totalPrice.setText(String.valueOf(total));
                    if (products != null) {
                        Log.d("TEST", String.valueOf(products.size()));
                        adapter.setData(products);  // Update the adapter's data
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    // Product retrieval failed, handle the failure
                    // ...
                }
            }

            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable t) {

            }
        });

    }


    }
//}