package com.example.moveyuiview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SuggestionsActivity extends AppCompatActivity {

    private MovieCardsAdapter adapter;
    SwipeFlingAdapterView flingContainer;
    List<BaseMovie> rowItems;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);
        Toolbar myToolbar = findViewById(R.id.topbar_suggestions);
        setSupportActionBar(myToolbar);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        BottomNavigationView navView = findViewById(R.id.bot_nav);
        setNavigationBarState(navView, R.id.suggestions);

        flingContainer = findViewById(R.id.frame);

        rowItems = new ArrayList<>();
        mQueue = Volley.newRequestQueue(this);

        //rowItems.add(new BaseMovie());
       // rowItems.add(new BaseMovie());
       // rowItems.add(new BaseMovie());
        //rowItems.add(new MovieCard("The Matrix","https://i.ytimg.com/vi/BsB62H0Q3V0/hqdefault.jpg"));
        // rowItems.add(new MovieCard("Shrek","https://www.shitpostbot.com/img/sourceimages/skintama-57d5903a4a3c4.jpeg"));
        // rowItems.add(new MovieCard("Shrek 2","https://zvukogram.com/upload/cimg-1-1610623877.jpg"));
        // rowItems.add(new MovieCard("Shrek 3","https://www.meme-arsenal.com/memes/657169b0e46e0f6bab53e79d4bc35438.jpg"));

        adapter = new MovieCardsAdapter(this, R.layout.movie_card, rowItems);
        fetchMovies(adapter);

        flingContainer.setAdapter(adapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Toast.makeText(SuggestionsActivity.this, "left", Toast.LENGTH_SHORT).show();
                CurrentContextHolder.getInstance().getCachedSavedMovies().add(rowItems.get(0));
                try {
                    saveSwipe(false, ((BaseMovie) dataObject).id, rowItems.get(0));
                } catch (AuthFailureError | JSONException authFailureError) {
                    authFailureError.printStackTrace();
                }
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                //Toast.makeText(SuggestionsActivity.this, "right", Toast.LENGTH_SHORT).show();
                CurrentContextHolder.getInstance().getCachedSavedMovies().add(rowItems.get(0));
                try {
                    saveSwipe(true, ((BaseMovie) dataObject).id , rowItems.get(0));
                } catch (AuthFailureError | JSONException authFailureError) {
                    authFailureError.printStackTrace();
                }
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                fetchMovies(adapter);
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });

        // Optionally add an OnItemClickListener
//        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClicked(int itemPosition, Object dataObject) {
//                Toast.makeText(SuggestionsActivity.this, "clicked", Toast.LENGTH_SHORT).show();
//                rowItems.remove(0);
//                try {
//                    Field privateField = SwipeFlingAdapterView.class.getDeclaredField("mActiveCard");
//                    privateField.setAccessible(true);
//                    privateField.set(flingContainer, null);
//                } catch (NoSuchFieldException | IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//                adapter.notifyDataSetChanged();
//            }
//        });

    }


    public void onUnwatchedButtonTapped(View view) {
        flingContainer.getTopCardListener().selectLeft();
        Button button = findViewById(R.id.unwatched_button);
    }

    public void onWatchedButtonTapped(View view) {
        flingContainer.getTopCardListener().selectRight();
        Button button = findViewById(R.id.watched_button);
    }

    public void onSavedButtonTapped(View view) {
        //Toast.makeText(SuggestionsActivity.this, "clicked", Toast.LENGTH_SHORT).show();
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
        Button button = findViewById(R.id.save_button);
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

    private void fetchMovies(final MovieCardsAdapter adapter) {
        String url = "http://192.168.49.2:80/movie/fetch";
        JsonObjectRequest stringRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.err.println(response.toString());
                        try {
                            adapter.addAll(new ObjectMapper().readValue(response.toString(), MovieResultsPage.class).results);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error!!!!!!!!!!!!" + error.getMessage());
            }
        });
        mQueue.add(stringRequest);
    }

    private void saveSwipe(final boolean liked, final Integer movieId, BaseMovie currentMovie) throws AuthFailureError, JSONException {
        System.out.println("IDIDIDDIDIDIDIDIIDIDID" + movieId);
        final Integer userId = 1;
        String url = "http://192.168.49.2:80/notification/swipes";
        JSONObject jsonBody = new JSONObject().put("userId", userId).put("movieId", movieId).put("liked", liked);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(SuggestionsActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SuggestionsActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        System.out.println(request.getBody().toString());
        System.out.println(request.getBodyContentType().toString());
        mQueue.add(request);
    }

    private void saveMovie(final Integer movieId) {
        final Integer userId = 1;
        String url = "http://192.168.49.2:80/notification/savings/" + userId + '/' + movieId;
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(SuggestionsActivity.this, "Added to 'Watch later'", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SuggestionsActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(request);
    }

    public static void LoadImage(String url, ImageView imageView) {
        ImageViewHelper viewHelper = new ImageViewHelper();
        try {
            Bitmap bitmap = viewHelper.execute(url).get();
            imageView.setImageBitmap(bitmap);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static class ImageViewHelper extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            URL url;
            HttpURLConnection httpURLConnection;
            InputStream inputStream;

            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                inputStream = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }

}