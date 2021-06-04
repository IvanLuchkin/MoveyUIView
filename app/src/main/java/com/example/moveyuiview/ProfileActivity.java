package com.example.moveyuiview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.ExecutionException;

public class ProfileActivity extends AppCompatActivity {

    private TextView username;
    private Button logout;
    private ImageView userImage;
    private SharedPreferences prefs;
    BottomNavigationView mNavView;

    public static void LoadImage(String url, ImageView imageView) {
        ImageViewHelper viewHelper = new ImageViewHelper();
        try {
            Bitmap bitmap = viewHelper.execute(url).get();
            imageView.setImageBitmap(bitmap);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        prefs = getSharedPreferences("Credentials", MODE_PRIVATE);
        username = findViewById(R.id.profile_user);
        username.setText(prefs.getString("username", "noname"));
        logout = findViewById(R.id.logout_button);
        userImage = findViewById(R.id.usr_image);
        LoadImage("http://cdn.onlinewebfonts.com/svg/img_227643.png", userImage);
        logout.setOnClickListener(v -> logout());
        mNavView = findViewById(R.id.bot_nav);
        Toolbar myToolbar = findViewById(R.id.topbar_profile);
        setSupportActionBar(myToolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setNavigationBarState(mNavView);
    }

    private void setNavigationBarState(BottomNavigationView navView) {
        navView.setSelectedItemId(R.id.profile);
        navView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.news) {
                Intent intent = new Intent(getApplicationContext(), WatchLaterActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            } else if (item.getItemId() == R.id.profile) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.suggestions) {
                Intent intent = new Intent(getApplicationContext(), SuggestionsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        overridePendingTransition(0, 0);
    }

    private void removeCurrentUserContext() {
        SharedPreferences.Editor edit = prefs.edit();
        edit.remove("username");
        edit.apply();
    }

    private void logout() {
        removeCurrentUserContext();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}