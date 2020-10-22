package com.example.moveyuiview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class SuggestionsActivity extends AppCompatActivity {

    private MovieCardsAdapter adapter;
    SwipeFlingAdapterView flingContainer;
    List<MovieCard> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);
        Toolbar myToolbar = findViewById(R.id.topbar_suggestions);
        setSupportActionBar(myToolbar);

        BottomNavigationView navView = findViewById(R.id.bot_nav);
        navView.setSelectedItemId(R.id.suggestions);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.news :
                        startActivity(new Intent(getApplicationContext(), NewsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile :
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.suggestions :
                       /*
                        startActivity(new Intent(getApplicationContext(), SuggestionsActivity.class));
                        overridePendingTransition(0, 0);
                        */
                        return true;
                }
                return false;
            }
        });

        flingContainer = findViewById(R.id.frame);

        rowItems = new ArrayList<>();
        rowItems.add(new MovieCard("Forrest Gump"));
        rowItems.add(new MovieCard("The Matrix"));

        adapter = new MovieCardsAdapter(this, R.layout.movie_card, rowItems);

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
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(SuggestionsActivity.this, "left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(SuggestionsActivity.this, "right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                /*
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);

                 */
            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(SuggestionsActivity.this, "clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onWatchedButtonTapped(View view) {
        flingContainer.getTopCardListener().selectRight();
        Button button = findViewById(R.id.watched_button);
    }

    public void onUnwatchedButtonTapped(View view) {
        flingContainer.getTopCardListener().selectLeft();
        Button button = findViewById(R.id.unwatched_button);
    }

    public void onSavedButtonTapped(View view) {

        Button button = findViewById(R.id.save_button);
    }
}