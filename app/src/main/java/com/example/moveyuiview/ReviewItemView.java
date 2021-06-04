package com.example.moveyuiview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.uwetrottmann.tmdb2.entities.Review;

@Layout(R.layout.review_copy)
public class ReviewItemView {

    private final Review mInfo;
    private final Context mContext;
    @View(R.id.username)
    private TextView username;
    @View(R.id.comment)
    private TextView content;
    @View(R.id.date)
    private TextView date;

    public ReviewItemView(Context context, Review review) {
        mContext = context;
        mInfo = review;
    }

    @SuppressLint("SetTextI18n")
    @Resolve
    private void onResolved() {
        username.setText(mInfo.author);
        content.setText(mInfo.content);
        date.setText(mInfo.iso_639_1);
    }
}
