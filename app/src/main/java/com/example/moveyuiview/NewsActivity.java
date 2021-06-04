package com.example.moveyuiview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mindorks.placeholderview.InfinitePlaceHolderView;
import com.uwetrottmann.tmdb2.entities.BaseMovie;

import java.util.List;

public class NewsActivity extends AppCompatActivity {

    private InfinitePlaceHolderView mLoadMoreView;
    private TextView noFilmsPlaceholder;
    private static final String NO_FILMS = "No Films";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Toolbar myToolbar = findViewById(R.id.topbar_news);
        setSupportActionBar(myToolbar);
        noFilmsPlaceholder = findViewById(R.id.no_saved_films);

        BottomNavigationView navView = findViewById(R.id.bot_nav);
        setNavigationBarState(navView,R.id.news);

        mLoadMoreView = findViewById(R.id.loadMoreView);
        setupView();
    }

    private void setupView(){
        Log.d("DEBUG", "LoadMoreView.LOAD_VIEW_SET_COUNT " + LoadMoreView.LOAD_VIEW_SET_COUNT);
        List<BaseMovie> feedList;
        if(CurrentContextHolder
                .getInstance().getCachedSavedMovies().size()!=0){
            feedList = CurrentContextHolder.getInstance().getCachedSavedMovies();
        }else {
            //Делаем фетч , если ничего нету то выводим NOFILMSl;
            noFilmsPlaceholder.setText(NO_FILMS);
            return;
        }
        int filmsMaxCount = 0;
        if(feedList.size() < LoadMoreView.LOAD_VIEW_SET_COUNT){
            filmsMaxCount = feedList.size();
        }else filmsMaxCount = LoadMoreView.LOAD_VIEW_SET_COUNT;
        for(int i = 0; i < filmsMaxCount; i++){
            mLoadMoreView.addView(new ItemView(this.getApplicationContext(), feedList.get(i)));
        }
        //mLoadMoreView.setLoadMoreResolver(new LoadMoreView(mLoadMoreView, feedList));
    }

    private void setNavigationBarState(BottomNavigationView navView, int currentButtonId){
        navView.setSelectedItemId(currentButtonId);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.news){
                    startActivity(new Intent(getApplicationContext(), NewsActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }else if(item.getItemId()==R.id.profile){
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }else if(item.getItemId()==R.id.suggestions){
                    startActivity(new Intent(getApplicationContext(), SuggestionsActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });
    }
}