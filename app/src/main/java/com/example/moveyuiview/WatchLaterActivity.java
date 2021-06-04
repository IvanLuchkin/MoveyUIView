package com.example.moveyuiview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mindorks.placeholderview.InfinitePlaceHolderView;
import com.uwetrottmann.tmdb2.entities.BaseMovie;

import java.util.ArrayList;
import java.util.List;

public class WatchLaterActivity extends AppCompatActivity {

    private static final String NO_FILMS = "No Films";
    private InfinitePlaceHolderView mLoadMoreView;
    private TextView noFilmsPlaceholder;
    private static RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Toolbar myToolbar = findViewById(R.id.topbar_news);
        setSupportActionBar(myToolbar);
        mQueue = Volley.newRequestQueue(this);
        noFilmsPlaceholder = findViewById(R.id.no_saved_films);

        BottomNavigationView navView = findViewById(R.id.bot_nav);
        setNavigationBarState(navView);

        mLoadMoreView = findViewById(R.id.loadMoreView);
        setupView();
    }

    private void setupView() {
        Log.d("DEBUG", "LoadMoreView.LOAD_VIEW_SET_COUNT " + SavedMoviesView.LOAD_VIEW_SET_COUNT);

        List<BaseMovie> feedList = new ArrayList<>();
        LoadMoreSavedMovies view = new LoadMoreSavedMovies(mLoadMoreView, feedList);
        if (CurrentContextHolder
                .getInstance().getCachedSavedMovies().size() != 0) {
            feedList = CurrentContextHolder.getInstance().getCachedSavedMovies();
        } else {
            mLoadMoreView.setLoadMoreResolver(new LoadMoreSavedMovies(mLoadMoreView, feedList));
            view.onLoadMore();
            //noFilmsPlaceholder.setText(NO_FILMS);
            return;
        }
        int filmsMaxCount = Math.min(feedList.size(), SavedMoviesView.LOAD_VIEW_SET_COUNT);
        for (int i = 0; i < filmsMaxCount; i++) {
            mLoadMoreView.addView(new MovieItemView(this.getApplicationContext(), feedList.get(i)));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        overridePendingTransition(0, 0);
    }

    private void setNavigationBarState(BottomNavigationView navView) {
        navView.setSelectedItemId(R.id.news);

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

    public static RequestQueue getmQueue() {
        return mQueue;
    }
}