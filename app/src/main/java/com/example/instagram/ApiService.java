package com.example.instagram;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("Customer")
    Call<List<Customer>> getCustomer(@Query("email") String email, @Query("password") String password);

//@GET("products")
//Call<List<Product>>  getProductDetail(@Query("id") int productId );

    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") int productId);
}
