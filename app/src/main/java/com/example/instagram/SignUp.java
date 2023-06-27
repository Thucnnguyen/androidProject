package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUp extends AppCompatActivity {

    private TextInputLayout fullNameLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout phoneLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout addressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullNameLayout = findViewById(R.id.full_name);

        emailLayout = findViewById(R.id.email);
        phoneLayout = findViewById(R.id.phone);
        passwordLayout = findViewById(R.id.password);
        addressLayout = findViewById(R.id.address);
        Button register = (Button) findViewById(R.id.sign_up_button);
        Button backLogin = (Button) findViewById(R.id.back_login_button);
        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private boolean ValidateName() {
        String val = fullNameLayout.getEditText().getText().toString();
        if (val.isEmpty()) {
            fullNameLayout.setError("Field cannot be empty");
            return false;
        } else {
            fullNameLayout.setError(null);
            fullNameLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean ValidateEmail() {
        String val = emailLayout.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        MutableLiveData<Boolean> check = new MutableLiveData<Boolean>();
        if (val.isEmpty()) {
            emailLayout.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            emailLayout.setError("Email is invalid");
            return false;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<Customer>> call = apiService.getCustomer();
        check.setValue(true);

        call.enqueue(new Callback<List<Customer>>() {

            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                if (response.isSuccessful()) {
                    List<Customer> customers = response.body();
                    if (customers != null && customers.size() > 0) {
                        for (Customer c : customers
                        ) {
                            if (c.getEmail().trim().equals(val.trim())) {
                                emailLayout.setError("Email is exist");
                                check.setValue(false);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {

            }
        });
        emailLayout.setError(null);
        return check.getValue();
    }

    private boolean ValidatePhone() {
        String val = phoneLayout.getEditText().getText().toString();
        if (val.isEmpty()) {
            phoneLayout.setError("Field cannot be empty");
            return false;
        } else {
            return true;
        }
    }

    private boolean ValidatePassword() {
        String val = passwordLayout.getEditText().getText().toString();

        if (val.isEmpty()) {
            passwordLayout.setError("Field cannot be empty");
            return false;
        } else {
            return true;
        }
    }

    private boolean ValidateAddress() {
        String val = addressLayout.getEditText().getText().toString();
        if (val.isEmpty()) {
            addressLayout.setError("Field cannot be empty");
            return false;
        } else {
            return true;
        }
    }

    public void registerUser(View view) {
        if (!ValidateName() | !ValidateEmail() | !ValidatePhone() | !ValidatePassword() | !ValidateAddress()) {
            return;
        }
        String fullName = fullNameLayout.getEditText().getText().toString();

        String email = emailLayout.getEditText().getText().toString();
        String phone = phoneLayout.getEditText().getText().toString();
        String password = passwordLayout.getEditText().getText().toString();
        String address = addressLayout.getEditText().getText().toString();
        Customer cus = new Customer(fullName, email, phone, address, password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6482d5d3f2e76ae1b95b92a6.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<Customer> call = apiService.register(cus);
        Intent intent = new Intent(this, Login.class);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (response.isSuccessful()) {
                    intent.putExtra("message", "Register Success!!");
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Toasty.error(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}