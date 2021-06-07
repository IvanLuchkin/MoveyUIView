package com.example.moveyuiview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    RequestQueue mQueue;
    private EditText username;
    private EditText password;
    private TextView errorField;
    private Button confirmLogin;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        CookieHandler.setDefault(new CookieManager(new PersistentCookieStore(this), CookiePolicy.ACCEPT_ALL));
        mQueue = Volley.newRequestQueue(this);
        prefs = getSharedPreferences("Credentials", MODE_PRIVATE);
        username = findViewById(R.id.username_field);
        password = findViewById(R.id.password_field);
        errorField = findViewById(R.id.error_field);
        confirmLogin = findViewById(R.id.login_btt);
        confirmLogin.setOnClickListener(v -> validate((username.getText().toString()), password.getText().toString()));

    }

    private void login(final String username, final String password) {
        String url = "http://192.168.49.2/user/login?username=" + username + "&password=" + password;
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(LoginActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();
                    System.out.println(response);
                }, error -> {
            System.out.println(new String((error.networkResponse.data)));
            Map<String, String> responseHeaders = error.networkResponse.headers;
            String rawCookies = responseHeaders.get("Set-Cookie");
            Log.i("cookies", rawCookies);
            Toast.makeText(LoginActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(error);
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Log.i("response", response.headers.toString());
                Map<String, String> responseHeaders = response.headers;
                String rawCookies = responseHeaders.get("Set-Cookie");
                Log.i("cookies", rawCookies);
                return super.parseNetworkResponse(response);
            }
        };
        mQueue.add(request);
    }

    private boolean validate(String username, String password) {
        //test();
        if (username.equalsIgnoreCase("admin")) {
            if (password.equalsIgnoreCase("admin")) {
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("username", username);
                edit.apply();
                login(username, password);
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                return true;
            }
        } else errorField.setText("Error. Please enter correct username and password");
        return false;
    }

}
