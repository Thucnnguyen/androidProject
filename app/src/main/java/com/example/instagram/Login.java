package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE);
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
        EditText userName = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.pass);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.getText().toString().equals("admin") && password.getText().toString().equals("123456")) {
                    editor.putBoolean("isLogin",true);
                    editor.apply();

                    Intent intent = new Intent(Login.this, HomeActivity.class);
                    intent.putExtra("name","MyAdmin");
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(Login.this,"login failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}