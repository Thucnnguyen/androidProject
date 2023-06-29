package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class activity_cartlist extends AppCompatActivity {
    public static activity_cartlist context;

    public ArrayList<Product> products = new ArrayList<>();

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
        Init();

    }
    private void toast(String msg) {
        if (toast != null) toast.cancel();
        toast = Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }


    public void GetCart() {

        Call call = apiService.getCartItems();

        call.enqueue(new Callback<Cart_items[]>() {
            @Override
            public void onResponse(Call<Cart_items[]> call, Response<Cart_items[]> response) {
                cart_items.clear();
                Collections.addAll(cart_items, response.body());
//                    CaclulateTotal();

                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Cart_items[]> call, Throwable t) {

            }
        });

    }
    public void Init() {

        Call call = apiService.getAllProducts();

        call.enqueue(new Callback<Product[]>() {
            @Override
            public void onResponse(Call<Product[]> call, Response<Product[]> response) {
                    Collections.addAll(products, response.body());
                cartAdapter = new CartAdapter(context, cart_items, products);

                rvCarts = findViewById(R.id.rcv);
                rvCarts.setAdapter(cartAdapter);
                rvCarts.setLayoutManager(new LinearLayoutManager(context));

                GetCart();
            }

            @Override
            public void onFailure(Call<Product[]> call, Throwable t) {
                String body = t.getMessage();
            }
        });

    }
    public void AddToCart(int productID, int quantity) {
        Customer customer = new Customer();
        String customerId = "customerId 2";
        int id = 1;

        Cart_items cart_items1 = new Cart_items(id,productID, quantity, customerId);
        apiService.addCartItems(cart_items1).enqueue(new Callback<Cart_items>() {
            @Override
            public void onResponse(Call<Cart_items> call, Response<Cart_items> response) {
                try {
                    if (!response.isSuccessful()) {
                        toast(response.errorBody().string());
                        return;
                    }

                    toast("Added item to cart!");
                    GetCart();
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Cart_items> call, Throwable t) {
                toast("failed");
            }
        });

    }

    public void RemoveFromCart(Cart_items cart, int quantity) {
//        cart.getQuantity() -= quantity;
        if (cart.getQuantity() > 0) {
            apiService.updateCartItems(cart.getProductID(), cart.getQuantity()).enqueue(new Callback<Cart_items>() {
                @Override
                public void onResponse(Call<Cart_items> call, Response<Cart_items> response) {
                    try {
                        if (!response.isSuccessful()) {
                            toast(response.errorBody().string());
                            return;
                        }

                        toast("Removed 1 from cart!");
//                        CaclulateTotal();
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
//                        CaclulateTotal();
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
//        tvTotal.setText("Total : " + carts.stream().mapToDouble(p -> p.quantity * p.product.price).sum());
//    }
}