package com.example.moveyuiview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private TextView errorField;
    private Button confirmLogin;
    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        prefs = getSharedPreferences("Credentials",MODE_PRIVATE);
        username = findViewById(R.id.username_field);
        password = findViewById(R.id.password_field);
        errorField = findViewById(R.id.error_field);
        confirmLogin= findViewById(R.id.login_btt);
        confirmLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate((username.getText().toString()),password.getText().toString());
            }
        });

    }

    private boolean validate(String username, String password){
        if(username.equalsIgnoreCase("admin")){
            if(password.equalsIgnoreCase("admin")){
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("username",username);
                edit.apply();
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                return true;
            }
        }else errorField.setText("Error. Please enter correct username and password");
        return false;
    }

}
