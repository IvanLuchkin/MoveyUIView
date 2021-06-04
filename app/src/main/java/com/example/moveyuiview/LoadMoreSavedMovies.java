package com.example.moveyuiview;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindorks.placeholderview.InfinitePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.infinite.LoadMore;
import com.uwetrottmann.tmdb2.entities.BaseMovie;
import com.uwetrottmann.tmdb2.entities.Movie;
import com.uwetrottmann.tmdb2.entities.Review;
import com.uwetrottmann.tmdb2.entities.ReviewResultsPage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Layout(R.layout.load_review_view)
public class LoadMoreSavedMovies {
    public static final int LOAD_VIEW_SET_COUNT = 6;
    public static long ACTUAL_REVIEWS_COUNT;

    private final InfinitePlaceHolderView mLoadMoreView;
    private final List<BaseMovie> mFeedList;

    public LoadMoreSavedMovies(InfinitePlaceHolderView loadMoreView, List<BaseMovie> feedList) {
        this.mLoadMoreView = loadMoreView;
        this.mFeedList = feedList;
    }

    @LoadMore
    public void onLoadMore() {
        Log.d("DEBUG", "onLoadMore");
        new LoadMoreSavedMovies.ForcedWaitedLoading();
    }

    class ForcedWaitedLoading implements Runnable {

        public ForcedWaitedLoading() {
            new Thread(this).start();
        }

        @Override
        public void run() {
            RequestFuture<JSONArray> requestFuture = RequestFuture.newFuture();
            try {
                final String url = "http://192.168.49.2:80/user/movie/saved";
                System.out.println(url);
                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                        url, new JSONArray(), requestFuture, requestFuture);
                WatchLaterActivity.getmQueue().add(request);
                System.out.println(WatchLaterActivity.getmQueue());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            try {
                JSONArray object = requestFuture.get(1, TimeUnit.SECONDS);
                System.out.println(object.toString());
                mFeedList.addAll(new ObjectMapper().readValue(object.toString(), new TypeReference<List<Movie>>(){}));
            } catch (InterruptedException | ExecutionException | TimeoutException | IOException e) {
                e.printStackTrace();
            }

            new Handler(Looper.getMainLooper()).post(() -> {

                for (BaseMovie movie : mFeedList) {
                    mLoadMoreView.addView(new MovieItemView(mLoadMoreView.getContext(), movie));
                }
                mLoadMoreView.noMoreToLoad();
                mLoadMoreView.loadingDone();
            });
        }
    }
}
