package com.example.moveyuiview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mindorks.placeholderview.InfinitePlaceHolderView;

import java.util.List;

public class NewsActivity extends AppCompatActivity {

    private InfinitePlaceHolderView mLoadMoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        BottomNavigationView navView = findViewById(R.id.bot_nav);
        navView.setSelectedItemId(R.id.news);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.news :
                        /*
                        startActivity(new Intent(getApplicationContext(), NewsActivity.class));
                        overridePendingTransition(0, 0);
                         */
                        return true;
                    case R.id.profile :
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.suggestions :
                        startActivity(new Intent(getApplicationContext(), SuggestionsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        mLoadMoreView = (InfinitePlaceHolderView)findViewById(R.id.loadMoreView);
        setupView();
    }

    private void setupView(){

        List<InfiniteFeedInfo> feedList = Utils.loadInfiniteFeeds(this.getApplicationContext());
        Log.d("DEBUG", "LoadMoreView.LOAD_VIEW_SET_COUNT " + LoadMoreView.LOAD_VIEW_SET_COUNT);
        for(int i = 0; i < LoadMoreView.LOAD_VIEW_SET_COUNT; i++){
            mLoadMoreView.addView(new ItemView(this.getApplicationContext(), feedList.get(i)));
        }
        mLoadMoreView.setLoadMoreResolver(new LoadMoreView(mLoadMoreView, feedList));
    }
}