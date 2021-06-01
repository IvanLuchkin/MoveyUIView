package com.example.moveyuiview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private TextView username;
    private Button logout;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        prefs = getSharedPreferences("Credentials",MODE_PRIVATE);
        username = findViewById(R.id.profile_user);
        username.setText(prefs.getString("username","noname"));
        logout = findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        BottomNavigationView navView = findViewById(R.id.bot_nav);
        setNavigationBarState(navView,R.id.profile);
        Toolbar myToolbar = findViewById(R.id.topbar_profile);
        setSupportActionBar(myToolbar);
    }

    private void setNavigationBarState(BottomNavigationView navView, int currentButtonId) {
        navView.setSelectedItemId(currentButtonId);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.news) {
                    startActivity(new Intent(getApplicationContext(), NewsActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (item.getItemId() == R.id.profile) {
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (item.getItemId() == R.id.suggestions) {
                    startActivity(new Intent(getApplicationContext(), SuggestionsActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });
    }

    private void removeCurrentUserContext(){
        SharedPreferences.Editor edit = prefs.edit();
        edit.remove("username");
        edit.apply();
    }

    private void logout(){
        removeCurrentUserContext();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}