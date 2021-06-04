package com.example.moveyuiview;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mindorks.placeholderview.InfinitePlaceHolderView;

public class LikedFilmsActivity extends AppCompatActivity {
    private InfinitePlaceHolderView mLoadMoreView;
    private Button backToProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_movies);

        backToProfile = findViewById(R.id.backToProfile);
        backToProfile.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            overridePendingTransition(0, 0);
        });

        mLoadMoreView = findViewById(R.id.loadReviewView);
    }
}
