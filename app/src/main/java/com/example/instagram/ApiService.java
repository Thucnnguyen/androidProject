package com.example.instagram;

import com.example.instagram.model.Customer;
import com.example.instagram.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @GET("products")
    Call<List<Product>> searchProductByName(@Query("name") String searchText);
}
