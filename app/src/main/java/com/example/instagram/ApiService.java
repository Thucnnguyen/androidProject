package com.example.instagram;

import java.util.List;

import okhttp3.ResponseBody;
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

}
