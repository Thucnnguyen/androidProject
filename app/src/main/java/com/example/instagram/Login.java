package com.example.instagram;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    SharedPreferences sharedPreferences;

    private GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean isLogin = sharedPreferences.getBoolean("isLogin", false);
        if (isLogin) {
            Intent intent = new Intent(Login.this, HomeActivity.class);
            intent.putExtra("name", "MyAdmin");
            startActivity(intent);
            finish();
            return;
        }
        if (getIntent() != null) {
            String mess = getIntent().getStringExtra("message");
            if (mess != null && !mess.isEmpty()) {
                Toasty.info(Login.this, mess).show();
            }
        }
        setContentView(R.layout.activity_login);
        Button button = (Button) findViewById(R.id.login_button);
        final int lightGrayColor = getResources().getColor(R.color.light_gray);
        TextView signUp = (TextView) findViewById(R.id.sign_up);
        EditText userName = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.pass);
        TextView ggLoginText = (TextView) findViewById(R.id.gg_text);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator colorAnimator = ObjectAnimator.ofInt(
                        button,
                        "backgroundColor",
                        Color.TRANSPARENT,
                        lightGrayColor
                        );
                colorAnimator.setDuration(200);
                colorAnimator.setEvaluator(new ArgbEvaluator());
                colorAnimator.start();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ApiService apiService = retrofit.create(ApiService.class);
                String email = userName.getText().toString();
                String password1 = password.getText().toString();
                Call<List<Customer>> call = apiService.getCustomer();
                call.enqueue(new Callback<List<Customer>>() {
                    @Override
                    public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                        if (response.isSuccessful()) {
                            boolean isExist = false;
                            List<Customer> customers = response.body();
                            if (customers != null && customers.size() > 0) {
                                for (Customer c : customers
                                ) {
                                    if (c.getEmail().trim().equals(email.trim()) && c.getPassword().trim().equals(password1.trim())) {
                                        // Assuming you only expect one customer
                                        String id = c.getId();
                                        String name = c.getName();
                                        loginSuccess(name, Integer.parseInt(id));
                                        break;
                                    }
                                }
                            }
                            if (!isExist) {
                                Toasty.error(Login.this, "Email Or password is wrong")
                                        .show();

                            }
                        } else {
                            loginFailed();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Customer>> call, Throwable t) {
                        // Handle failure
                        loginFailed();
                        Log.d("CREATION", "onFailure: " + t.getMessage());
                    }
                });
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);

                startActivity(intent);
                finish();
            }
        });

        ggLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String email = account.getEmail();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ApiService apiService = retrofit.create(ApiService.class);

                Call<List<Customer>> call = apiService.getCustomer();
                call.enqueue(new Callback<List<Customer>>() {
                    @Override
                    public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                        if (response.isSuccessful()) {
                            boolean isExist = false;
                            Customer cus = null;
                            List<Customer> customers = response.body();
                            if (customers != null && customers.size() > 0) {
                                for (Customer c : customers) {
                                    if (c.getEmail().trim().equals(email.trim())) {
                                        isExist = true;
                                        cus=c;
                                        break;
                                    }
                                }
                            }

                            if (!isExist) {
                                String fullName = account.getDisplayName();
                                // The email doesn't exist in the database, save it
                                Customer customer = new Customer();
                                customer.setEmail(email);
                                customer.setName(fullName);
                                Call<Customer> registerCall = apiService.register(customer);
                                registerCall.enqueue(new Callback<Customer>() {
                                    @Override
                                    public void onResponse(Call<Customer> call, Response<Customer> response) {
                                        if (response.isSuccessful()) {
                                            Customer registeredCustomer = response.body();
                                            // Email saved successfully
                                            loginSuccess(account.getDisplayName(), Integer.parseInt(registeredCustomer.getId()));
                                        } else {
                                            // Failed to save email
                                            loginFailed();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Customer> call, Throwable t) {
                                        // Handle failure
                                        loginFailed();
                                        Log.d("CREATION", "onFailure: " + t.getMessage());
                                    }
                                });
                            } else {
                                // Email already exists in the database
                                loginSuccess(account.getDisplayName(), Integer.parseInt(cus.getId()));
                            }
                        } else {
                            loginFailed();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Customer>> call, Throwable t) {
                        // Handle failure
                        loginFailed();
                        Log.d("CREATION", "onFailure: " + t.getMessage());
                    }
                });
            } catch (ApiException e) {
                // Google Sign In failed, handle the error
                // ...
            }
        }
    }


    private void loginSuccess(String name, int id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin", true);
        editor.putInt("customerId", id);
        editor.apply();

        Intent intent = new Intent(Login.this, HomeActivity.class);
        intent.putExtra("name", name);
        startActivity(intent);
        finish();
    }

    private void loginFailed() {
        Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
    }

}