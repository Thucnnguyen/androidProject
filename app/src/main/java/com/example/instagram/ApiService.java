package com.example.instagram;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("Customer")
    Call<List<Customer>> getCustomer();
    @POST("Customer")
    Call<Customer> register(@Body Customer customer);

    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") int productId);

    @GET("products")
    Call<List<Product>> getAllProduct();
    @GET("Cart/{id}")
    Call<Cart> getCart(@Body String id);
    @GET("Cart_items")
    Call<Cart_items[]> getCartItems();

    @POST("Cart_items")
    Call<Cart_items> addCartItems(@Query("ProductID") int ProductID,
                                  @Query("Quantity") int Quantity);


    @PUT("Cart_items")
    Call<Cart_items> updateCartItems(@Query("ProductID") int ProductID,
                                     @Query("Quantity") int Quantity);

    @DELETE("Cart_items")

    Call<Void> deleteCartItems(@Query("ProductID") int ProductID);
}
