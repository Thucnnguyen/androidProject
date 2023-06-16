package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    SharedPreferences sharedPreferences ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean isLogin = sharedPreferences.getBoolean("isLogin", false);
        if(isLogin){
            Intent intent = new Intent(Login.this, HomeActivity.class);
            intent.putExtra("name","MyAdmin");
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.activity_login);
        Button button = (Button) findViewById(R.id.login_button);
        TextView signUp = (TextView) findViewById(R.id.sign_up);
        EditText userName = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.pass);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ApiService apiService = retrofit.create(ApiService.class);
                String username =userName.getText().toString();
                String password1 =password.getText().toString();
                Call<List<Customer>> call = apiService.getCustomer(username,password1);
                call.enqueue(new Callback<List<Customer>>() { // Update the response type to List<Customer>
                    @Override
                    public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                        if (response.isSuccessful()) {
                            List<Customer> customers = response.body();
                            if (customers != null && customers.size() > 0) {
                                Customer customer = customers.get(0); // Assuming you only expect one customer
                                String id = customer.getId();
                                String name = customer.getName();
                                loginSuccess(name, Integer.parseInt(id));
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

    }
    private void loginSuccess(String name,int id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin", true);
        editor.putInt("customerId",id);
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