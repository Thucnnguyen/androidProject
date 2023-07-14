package com.example.instagram;

import com.example.instagram.model.Cart;
import com.example.instagram.model.Customer;
import com.example.instagram.model.Order;
import com.example.instagram.model.OrderItem;
import com.example.instagram.model.Product;

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
    @GET("Customer")
    Call<List<Customer>> getCustomerByEmail(@Query("email") String email);
    @GET("Customer/{id}")
    Call<Customer> getCustomerById(@Path("id") String id);
    @PUT("Customer/{id}")
    Call<Customer> update(@Body Customer customer, @Path("id") String id);
    @POST("Customer")
    Call<Customer> register(@Body Customer customer);

    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") int productId);

    @GET("products")
    Call<List<Product>> getAllProduct();


    @GET("products")
    Call<List<Product>> searchProductByName(@Query("name") String searchText);


    @GET("Cart/{id}")
    Call<Cart> getCart(@Body String id);
    @GET("Cart_items")
    Call<List<Cart_items>> getCartItems();

    @PUT("Cart_items/{id}")
    Call<ResponseBody> updateCartItems(@Path("id")int cartId, @Body Cart_items cart_items);

    @POST("Cart_items")
    Call<Cart_items> addCartItems(@Body Cart_items cart_items);

    @PUT("Cart_items")
    Call<Cart_items> updateCartItems(@Query("ProductID") int ProductID,
                                     @Query("Quantity") int Quantity);

    @DELETE("Cart_items")

    Call<Void> deleteCartItems(@Query("ProductID") int ProductID);


    @GET("Orders")
    Call<List<Order>> getOrders();
    @GET("Orders")
    Call<List<Order>> getOrdersByCustomerId(@Query("customer_id") int customerId);
//    @GET("Order_items")
//    Call<List<OrderItem>> getOrderItemsByOrderId(@Query("order_id") int orderId);


    @GET("Order_items")
    Call<List<OrderItem>> getOrderItemsByOrderId(@Query("order_id") int orderId);

}
