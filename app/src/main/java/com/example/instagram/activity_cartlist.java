package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.instagram.model.Customer;
import com.example.instagram.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class activity_cartlist extends AppCompatActivity {
    public static activity_cartlist context;
    Customer customer = new Customer();
    int customerId;
    public List<Product> products = new ArrayList<>();
    private Button checkOut;
    Toast toast;
    CartAdapter cartAdapter;
    RecyclerView rvCarts;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ApiService apiService = retrofit.create(ApiService.class);
    ArrayList<Cart_items> cart_items = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartlist);
        context= this;
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE);
        customerId = sharedPreferences.getInt("customerId", 0);
        Init();

    }
    private void toast(String msg) {
        if (toast != null) toast.cancel();
        toast = Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }


    public void GetCart() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE);
        int cusId =sharedPreferences.getInt("customerId", -1);
        Call<List<Cart_items>> call = apiService.getCartItems();

        call.enqueue(new Callback<List<Cart_items>>() {
            @Override
            public void onResponse(Call<List<Cart_items>> call, Response<List<Cart_items>> response) {

                if(response.isSuccessful()){
                    List<Cart_items> cartItems= response.body();
                    for (Cart_items c: cartItems
                         ) {
                        if(c.getCustomerId() == cusId){
                            Log.d("cartItem_info", c.toString());
                            cart_items.add(c);
                        }
                    }
                }
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Cart_items>> call, Throwable t) {

            }
        });

    }
    public void Init() {
        rvCarts = findViewById(R.id.rcv);
        rvCarts.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(context, cart_items, products);
        rvCarts.setAdapter(cartAdapter);
        checkOut = (Button) findViewById(R.id.checkout);
        Call call = apiService.getAllProduct();
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_cartlist.this, PaymentActivity.class);
                startActivity(intent);
                finish();
            }
        });
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                products.clear();
                products.addAll(response.body());
                Log.d("data", products.toString());
                cartAdapter.notifyDataSetChanged();
                GetCart();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                String body = t.getMessage();
            }
        });

    }
    public void AddToCart(Cart_items cart, int quantity) {
        cart.Quantity += quantity;
        apiService.updateCartItems2(cart.getId(),cart).enqueue(new Callback<Cart_items>() {
            @Override
            public void onResponse(Call<Cart_items> call, Response<Cart_items> response) {
                try {
                    if (!response.isSuccessful()) {
                        toast(response.errorBody().string());
                        return;
                    }
                    cartAdapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Cart_items> call, Throwable t) {

            }
        });
    }

    public void RemoveFromCart(Cart_items cart, int quantity) {
        cart.Quantity -= quantity;
        if (cart.getQuantity() > 0) {
            apiService.updateCartItems2(cart.getId(),cart).enqueue(new Callback<Cart_items>() {
                @Override
                public void onResponse(Call<Cart_items> call, Response<Cart_items> response) {
                    try {
                        if (!response.isSuccessful()) {
                            toast(response.message());
                            return;
                        }
                        cartAdapter.notifyDataSetChanged();
                    } catch (Exception e) {

                    }
                }
                @Override
                public void onFailure(Call<Cart_items> call, Throwable t) {

                }
            });
        } else {
            Call<Void> call = apiService.deleteCartItems(cart.getProductID());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    try {
                        if (!response.isSuccessful()) {
                            toast(response.errorBody().string());
                            return;
                        }

                        toast("Deleted item from cart!");
                        products.remove(cart);
                        cartAdapter.notifyDataSetChanged();
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }
    }

//    public void CaclulateTotal() {
//        TextView total = findViewById(R.id.total);
//        Product product = new Product();
//        Cart_items cart = new Cart_items();
//        int overTotalPrice = 0;
//        int oneTypeProductPrice = (int) (((Float.valueOf(product.getPrice())) * Integer.valueOf(cart.getQuantity())));
//        overTotalPrice = oneTypeProductPrice + overTotalPrice;
//        total.setText("Total: " + Integer.valueOf(overTotalPrice) +"$");
//    }
}