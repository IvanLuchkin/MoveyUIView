package com.example.moveyuiview;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.uwetrottmann.tmdb2.entities.BaseMovie;

@Layout(R.layout.load_more_item_view)
public class MovieItemView {

    private final BaseMovie mInfo;
    private final Context mContext;
    @View(R.id.username)
    private TextView titleTxt;
    @View(R.id.comment)
    private TextView captionTxt;
    @View(R.id.date)
    private TextView timeTxt;
    @View(R.id.newsImageView)
    private ImageView imageView;

    public MovieItemView(Context context, BaseMovie info) {
        mContext = context;
        mInfo = info;
    }

    @Resolve
    private void onResolved() {
        titleTxt.setText(mInfo.title);
        captionTxt.setText(mInfo.overview);
        timeTxt.setText(mInfo.release_date == null ? "-" : mInfo.release_date.toString());
        imageView.setClickable(true);
        imageView.setOnClickListener(v -> {
            CurrentContextHolder.getInstance().setLastKnownMovieId(mInfo.id);
            Intent intent = new Intent(mContext, ReviewsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });
        Glide.with(mContext).load(mInfo.poster_path).into(imageView);
    }
}
