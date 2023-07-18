package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.instagram.model.Order;
import com.example.instagram.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderActivity extends AppCompatActivity {

    /// test hard code cutomer id
        int customerID = 51;
    /// test hard code cutomer id


    private RecyclerView rvItem;
    private OrderListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        rvItem = findViewById(R.id.rcv_user);
        adapter = new OrderListAdapter(this);

//        GridLayoutManager grid = new GridLayoutManager(this, 2);
//        rvItem.setLayoutManager(grid);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvItem.setLayoutManager(layoutManager);

        getListItem();
        rvItem.setAdapter(adapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void getListItem() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<Order>> call = apiService.getOrdersByCustomerId(customerID);
//        Call<List<Order>> call = apiService.getOrders();
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    List<Order> products = response.body();

                    List<Order> fillterOrder = new ArrayList<>();

                    for (Order item : products) {
                        if (item.getCustomerId() == customerID) {
                            fillterOrder.add(item);
                        }
                    }



                    if (fillterOrder != null ) {
//                        Log.d("TEST", String.valueOf(products.get(0).getOderStatus()));
                        adapter.setData(fillterOrder);  // Update the adapter's data
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    // Product retrieval failed, handle the failure
                    // ...
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.d("error kia", "Error: " + t.getMessage());
                t.printStackTrace();
            }
        });

    }

}