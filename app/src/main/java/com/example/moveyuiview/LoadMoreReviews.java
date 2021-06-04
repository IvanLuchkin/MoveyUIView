package com.example.moveyuiview;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindorks.placeholderview.InfinitePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.infinite.LoadMore;
import com.uwetrottmann.tmdb2.entities.Review;
import com.uwetrottmann.tmdb2.entities.ReviewResultsPage;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Layout(R.layout.load_review_view)
public class LoadMoreReviews {
    public static final int LOAD_VIEW_SET_COUNT = 6;
    public static long ACTUAL_REVIES_COUNT;

    private final InfinitePlaceHolderView mLoadMoreView;
    private final List<Review> mFeedList;

    public LoadMoreReviews(InfinitePlaceHolderView loadMoreView, List<Review> feedList) {
        this.mLoadMoreView = loadMoreView;
        this.mFeedList = feedList;
    }

    @LoadMore
    public void onLoadMore() {
        Log.d("DEBUG", "onLoadMore");
        new LoadMoreReviews.ForcedWaitedLoading();
    }

    class ForcedWaitedLoading implements Runnable {

        public ForcedWaitedLoading() {
            new Thread(this).start();
        }

        @Override
        public void run() {
            RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();
            try {
                final String url = "http://192.168.49.2/review/" + CurrentContextHolder.getInstance().getLastKnownMovieId();
                System.out.println(url);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                        url, new JSONObject(), requestFuture, requestFuture);
                ReviewsActivity.getmQueue().add(request);
                System.out.println(ReviewsActivity.getmQueue());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            try {
                JSONObject object = requestFuture.get(1, TimeUnit.SECONDS);
                System.out.println(object.toString());
                mFeedList.addAll(new ObjectMapper().readValue(object.toString(), ReviewResultsPage.class).results);
                ACTUAL_REVIES_COUNT = mFeedList.stream().count();
            } catch (InterruptedException | ExecutionException | TimeoutException | IOException e) {
                e.printStackTrace();
            }

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    for (Review review : mFeedList) {
                        mLoadMoreView.addView(new ReviewItemView(mLoadMoreView.getContext(), review));
                    }
                    mLoadMoreView.noMoreToLoad();
                    mLoadMoreView.loadingDone();
//                    //int count = mLoadMoreView.getViewCount();
//                    int count = mFeedList.size();
//                    Log.d("DEBUG", "count " + count);
//                    for (int i = count - 1;
//                         i < (count - 1 + LoadMoreReviews.LOAD_VIEW_SET_COUNT) && mFeedList.size() > i;
//                         i++) {
//                        mLoadMoreView.addView(new ReviewItemView(mLoadMoreView.getContext(), mFeedList.get(i)));
//
//                        if(i == mFeedList.size()){
//                            mLoadMoreView.noMoreToLoad();
//                            break;
//                        }
//                    }
//                    mLoadMoreView.loadingDone();
                }
            });
        }
    }
}
