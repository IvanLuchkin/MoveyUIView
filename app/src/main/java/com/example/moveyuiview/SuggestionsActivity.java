package com.example.moveyuiview;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.uwetrottmann.tmdb2.entities.BaseMovie;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuggestionsActivity extends AppCompatActivity {

    SwipeFlingAdapterView flingContainer;
    List<BaseMovie> rowItems;
    private MovieCardsAdapter adapter;
    private RequestQueue mQueue;
    BottomNavigationView mNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);

        Toolbar myToolbar = findViewById(R.id.topbar_suggestions);
        setSupportActionBar(myToolbar);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mNavView = findViewById(R.id.bot_nav);

        flingContainer = findViewById(R.id.frame);

        rowItems = new ArrayList<>();
        mQueue = Volley.newRequestQueue(this);
        if (CurrentContextHolder.getInstance().isIsFirstTimeOpenedSuggestions()) {
            adapter = new MovieCardsAdapter(this, R.layout.movie_card, rowItems);
            CurrentContextHolder.getInstance().setIsFirstTimeOpenedSuggestions(false);
            fetchMovies(adapter);
        } else {
            rowItems = CurrentContextHolder.getInstance().getSuggestionMoviesCache();
            adapter = new MovieCardsAdapter(this, R.layout.movie_card, rowItems);
        }


        flingContainer.setAdapter(adapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                adapter.notifyDataSetChanged();
                updateSuggestionCacheMovies();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                try {
                    saveSwipe(false, ((BaseMovie) dataObject).id, rowItems.get(0));
                } catch (AuthFailureError | JSONException authFailureError) {
                    authFailureError.printStackTrace();
                }
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                try {
                    saveSwipe(true, ((BaseMovie) dataObject).id, rowItems.get(0));
                } catch (AuthFailureError | JSONException authFailureError) {
                    authFailureError.printStackTrace();
                }
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                //  rowItems.add(new BaseMovie());  //delete in prod
                // rowItems.add(new BaseMovie());   //delete in prod
                fetchMovies(adapter);
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setNavigationBarState(mNavView);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        overridePendingTransition(0, 0);
    }

    public void onUnwatchedButtonTapped(View view) {
        flingContainer.getTopCardListener().selectLeft();
    }

    public void onWatchedButtonTapped(View view) {
        flingContainer.getTopCardListener().selectRight();
    }

    public void onSavedButtonTapped(View view) {
        CurrentContextHolder.getInstance().getCachedSavedMovies().add(rowItems.get(0));
        saveMovie(rowItems.get(0).id);
        rowItems.remove(0);
        try {
            Field privateField = SwipeFlingAdapterView.class.getDeclaredField("mActiveCard");
            privateField.setAccessible(true);
            privateField.set(flingContainer, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
        updateSuggestionCacheMovies();
    }

    private void updateSuggestionCacheMovies() {
        CurrentContextHolder.getInstance().getSuggestionMoviesCache().clear();
        CurrentContextHolder.getInstance().getSuggestionMoviesCache().addAll(rowItems);
    }

    private void setNavigationBarState(BottomNavigationView navView) {
        navView.setSelectedItemId(R.id.suggestions);
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

    private void fetchMovies(final MovieCardsAdapter adapter) {
        String url = "http://192.168.49.2:80/user/movie/fetch";
        JsonObjectRequest stringRequest = new JsonObjectRequest(url, null,
                response -> {
                    try {
                        List<BaseMovie> movies = new ObjectMapper().readValue(response.toString(), MovieResultsPage.class).results;
                        adapter.addAll(movies);
                        CurrentContextHolder.getInstance().getSuggestionMoviesCache().addAll(movies);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, error -> System.err.println("Error" + error.getMessage()));
        mQueue.add(stringRequest);
    }

    private void saveSwipe(final boolean liked, final Integer movieId, BaseMovie currentMovie) throws AuthFailureError, JSONException {
        String url = "http://192.168.49.2:80/user/swipes";
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("movieId", movieId);
        jsonBody.put("liked", liked);
        System.out.println(jsonBody.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> Toast.makeText(SuggestionsActivity.this, "Saved!", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(SuggestionsActivity.this, "Error!", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                return Response.success(null, null);
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        mQueue.add(request);
    }

    private void saveMovie(final Integer movieId) {
        String url = "http://192.168.49.2:80/user/notification/savings/" + movieId;
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(SuggestionsActivity.this, "Added to 'Watch later'", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(SuggestionsActivity.this, "Error!", Toast.LENGTH_SHORT).show());
        mQueue.add(request);
    }
}
