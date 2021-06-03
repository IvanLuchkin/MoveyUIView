package com.example.moveyuiview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.mindorks.placeholderview.InfinitePlaceHolderView;
import com.uwetrottmann.tmdb2.entities.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewsActivity extends AppCompatActivity {
        private InfinitePlaceHolderView mLoadMoreView;
        private Button backButton;
        private static RequestQueue mQueue;

    public static RequestQueue getmQueue() {
        return mQueue;
    }

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_film_review);
            backButton = findViewById(R.id.back_to_liked_films);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), NewsActivity.class));

                }
            });
            Toolbar myToolbar = findViewById(R.id.topbar_news);
            setSupportActionBar(myToolbar);
            mQueue = Volley.newRequestQueue(this);

            mLoadMoreView = findViewById(R.id.loadReviewView);
            setupView();
        }

        private void setupView(){
//            List<ReviewInfo> feedList = Utils.loadInfiniteReviews(this.getApplicationContext());
            Log.d("DEBUG", "LoadMoreView.LOAD_VIEW_SET_COUNT " + LoadMoreReviews.LOAD_VIEW_SET_COUNT);
//            for(int i = 0; i < LoadMoreReviews.LOAD_VIEW_SET_COUNT; i++){
//                mLoadMoreView.addView(new ReviewItemView(this.getApplicationContext(), feedList.get(i)));
//            }
            LoadMoreReviews view = new LoadMoreReviews(mLoadMoreView, new ArrayList<Review>());
            mLoadMoreView.setLoadMoreResolver(view);
            view.onLoadMore();

        }



}
