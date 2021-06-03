package com.example.moveyuiview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

@Layout(R.layout.review_copy)
public class ReviewItemView {

    @View(R.id.username)
    private TextView username;

    @View(R.id.comment)
    private TextView content;

    @View(R.id.date)
    private TextView date;

    private final ReviewInfo mInfo;
    private final Context mContext;

    public ReviewItemView(Context context, ReviewInfo info) {
        mContext = context;
        mInfo = info;
    }

    @SuppressLint("SetTextI18n")
    @Resolve
    private void onResolved() {
        username.setText("Yarik");
        content.setText("Nice at all");
        date.setText("06 April 2001");
    }
}
