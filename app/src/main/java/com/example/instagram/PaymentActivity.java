package com.example.instagram;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;


import android.widget.Toast;

import com.example.instagram.model.Customer;
import com.example.instagram.model.Order;
import com.example.instagram.model.Order_Item;
import com.example.instagram.model.Product;

import com.google.gson.JsonObject;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.config.PaymentButtonIntent;
import com.paypal.checkout.config.SettingsConfig;
import com.paypal.checkout.config.UIConfig;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.OrderRequest;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PaymentButtonContainer;
import com.paypal.pyplcheckout.ui.utils.ReturnToProviderOperationType;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentActivity extends AppCompatActivity {
    private static final String YOUR_CLIENT_ID = "AbPM6WtYhSLQjIN26c0sGrv3wDDA25gYnu4YmDoWMgag9heElje0_hQ0acfAVIZDNugKlcqMfe1EbYWY";
    private RecyclerView recyclerViewProducts;
    private TextView textViewTotalPrice, textViewCustName, textViewCustPhone, textviewCustAddr;

    private RadioButton paypalBtn, shipCodBtn;
    private RadioGroup radioGroup;

    int PAYPAL_REQUEST_CODE = 123;
    SharedPreferences sharedPreferences;
    private Button paymentButton, btnReturn;

    public static PayPalConfiguration configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_payment);
        textViewCustName = findViewById(R.id.textViewUserName);
        textViewCustPhone = findViewById(R.id.textViewPhone);
        textviewCustAddr = findViewById(R.id.textViewAddr);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        paymentButton = findViewById(R.id.payment_button_container);
        btnReturn = findViewById(R.id.button_return);
        paypalBtn = findViewById(R.id.radioButtonPaypal);
        shipCodBtn = findViewById(R.id.radioButtonShipCOD);
        radioGroup = findViewById(R.id.radioGroupPaymentMethod);


        int customerId = sharedPreferences.getInt("customerId", 0);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


// Disable the submit button initially
        paymentButton.setEnabled(false);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Enable the submit button when a radio button is checked
                paymentButton.setEnabled(true);
            }
        });

        configuration = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(YOUR_CLIENT_ID);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(intent);


        //Paypal config
        PayPalCheckout.setConfig(new CheckoutConfig(
                getApplication(),
                YOUR_CLIENT_ID,
                Environment.SANDBOX,
                CurrencyCode.USD,
                UserAction.PAY_NOW,
                PaymentButtonIntent.CAPTURE,
                new SettingsConfig(true, false),
                new UIConfig(true),
                "com.example.instagram" + "://paypalpay"
        ));

        // Retrofit initialization
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of the API service
        ApiService apiService = retrofit.create(ApiService.class);
        // Call the API endpoint
        Call call = apiService.getCartItems();

        // Execute the API call asynchronously
        call.enqueue(new Callback<List<Cart_items>>() {
            @Override
            public void onResponse(Call<List<Cart_items>> call, Response<List<Cart_items>> response) {
                if (response.isSuccessful()) {

                    List<Cart_items> items = response.body();
                    Call productCall = apiService.getAllProduct();

                    productCall.enqueue(new Callback<List<Product>>() {
                        @Override
                        public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                            if (response.isSuccessful()) {
                                List<Product> products = response.body();
                                // Process the retrieved products here
                                // Create a LinearLayoutManager with the desired orientation
                                LinearLayoutManager layoutManager = new LinearLayoutManager(PaymentActivity.this, LinearLayoutManager.VERTICAL, false);
                                recyclerViewProducts.setLayoutManager(layoutManager);

                                // Set the adapter for the RecyclerView
                                ProductAdapter adapter = new ProductAdapter(PaymentActivity.this, products, items, customerId);
                                recyclerViewProducts.setAdapter(adapter);

                                // Calculate the total price
                                double totalPrice = calculateTotalPrice(products, items, customerId);
                                // Set the total price in the TextView
                                textViewTotalPrice.setText("Total Price: $" + totalPrice);

                                paymentButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                                        // Get the checked radio button's ID from the RadioGroup
//                                        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();

                                        // Perform the action based on the selected radio button
                                        if (paypalBtn.isChecked()) {
                                            getPayment("" + totalPrice);
                                        } else if (shipCodBtn.isChecked()) {
                                            AddToOrder(items, products, customerId);
//                                            Call<List<Order>> orderCall = apiService.getOrder();
//
//                                            orderCall.enqueue(new Callback<List<Order>>() {
//                                                @Override
//                                                public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
//                                                    if (response.isSuccessful()) {
//                                                        List<Order> orders = response.body();
//                                                        for (Order order : orders) {
//
//                                                            if (order.getCustomer_id() == customerId) {
//                                                                Toast.makeText(PaymentActivity.this, "" + order.getId(), Toast.LENGTH_SHORT).show();
//                                                                AddOrderItem(items, products, customerId, order.getId());
//                                                            }
//                                                        }
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onFailure(Call<List<Order>> call, Throwable t) {
//                                                    Toast.makeText(PaymentActivity.this, "Failed =(((" , Toast.LENGTH_SHORT).show();
//                                                }
//                                            });


                                            Intent intent = new Intent(PaymentActivity.this, ProductList.class);
                                            startActivity(intent);
                                        }

                                        // Disable the submit button after processing the selection
                                        paymentButton.setEnabled(false);
                                    }
                                });
//                                paymentButton.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        getPayment("" + totalPrice);
//                                    }
//                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Product>> call, Throwable t) {
                            Log.d("CREATION", "onFailure: " + t.getMessage());
                            Toast.makeText(PaymentActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                        }
                    });


                } else {
                    // Handle error response
                    Toast.makeText(PaymentActivity.this, "Failed to retrieve products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Cart_items>> call, Throwable t) {
                // Handle network errors or API call failures
                Toast.makeText(PaymentActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                Toast.makeText(PaymentActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
        //Get Customer Info
//        String customerId = "1";


        call = apiService.getCustomerById("" + customerId);

        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (response.isSuccessful()) {
                    Customer customer = response.body();
                    if (customer != null) {
                        BindingData(customer);
                    }
                } else {
                    // Product retrieval failed, handle the failure
                    Toast.makeText(PaymentActivity.this, "Failed to retrieve customer information", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                // Handle failure
                Toast.makeText(PaymentActivity.this, "Failed to retrieve product", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPayment(String price) {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(price)), "USD", "Total Price:", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, com.paypal.android.sdk.payments.PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, PAYPAL_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);


                if (paymentConfirmation != null) {
                    try {
                        String paymentDetails = paymentConfirmation.toJSONObject().toString();
                        JSONObject object = new JSONObject(paymentDetails);
                    } catch (JSONException e) {
                        Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (requestCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "The user canceled.", Toast.LENGTH_SHORT).show();
            } else if (requestCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(this, "Invalid Payment!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private double calculateTotalPrice(List<Product> productList, List<Cart_items> items, int customerId) {
        float totalPrice = 0.0f;

        // Iterate through the product list and sum up the prices
        for (Cart_items item : items) {
            if (item.getCustomerId() == customerId) {
                for (Product product : productList) {
                    if (item.getProductID() == product.getId()) {
                        totalPrice += product.getPrice() * item.getQuantity();
                    }
                }
            }
        }
        return totalPrice;
    }

    private void BindingData(Customer cus) {

        textViewCustName.setText(cus.getName());
        textviewCustAddr.setText(cus.getAddress());
        textViewCustPhone.setText(cus.getPhone());
    }

    private void DeleteCart(List<Cart_items> items, int customerId) {
        // Retrofit initialization
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Create an instance of the API service
        ApiService apiService = retrofit.create(ApiService.class);
        // Call the API endpoint
        Call<Void> call;
        for (Cart_items item : items) {
            if (item.getCustomerId() == customerId) {
                call = apiService.deleteCartItemsById("" + item.getId());

                // Execute the API call asynchronously
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        }

    }

    private void AddToOrder(List<Cart_items> items, List<Product> products, int customerId) {
        Calendar calendar = Calendar.getInstance();
        Order order = new Order(customerId, "Processing", calendar.getTime().toString());
        // Retrofit initialization
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Create an instance of the API service
        ApiService apiService = retrofit.create(ApiService.class);
        // Call the API endpoint
        Call<Order> call = apiService.addOrder(order);


        // Execute the API call asynchronously
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    Order order = response.body();
                    AddOrderItem(items, products, customerId, order.getId());
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {

            }
        });

    }

    private void AddOrderItem(List<Cart_items> items, List<Product> products, int customerId, int order_id) {
        Order_Item order_item = new Order_Item();
        // Retrofit initialization
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Create an instance of the API service
        ApiService apiService = retrofit.create(ApiService.class);
        for (Cart_items item : items) {
            if (item.getCustomerId() == customerId) {
                for (Product product : products) {
                    if (item.getProductID() == product.getId()) {
                        order_item = new Order_Item(order_id, item.getProductID(), item.getQuantity(), item.getQuantity() * product.getPrice());
                        // Call the API endpoint
                        Call call = apiService.addOrderItem(order_item);
                        call.enqueue(new Callback<Order_Item>() {
                            @Override
                            public void onResponse(Call<Order_Item> call, Response<Order_Item> response) {
                                if (response.isSuccessful()) {
                                    DeleteCart(items, customerId);
                                }
                            }

                            @Override
                            public void onFailure(Call<Order_Item> call, Throwable t) {

                            }
                        });
                    }
                }


            }
        }

    }

}
