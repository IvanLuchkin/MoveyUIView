package com.example.moveyuiview;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

@Layout(R.layout.load_more_view)
public class MovieItemView {

    @View(R.id.username)
    private TextView titleTxt;

    @View(R.id.comment)
    private TextView captionTxt;

    @View(R.id.date)
    private TextView timeTxt;

    @View(R.id.newsImageView)
    private ImageView imageView;

    private final InfiniteFeedInfo mInfo;
    private final Context mContext;

    public MovieItemView(Context context, InfiniteFeedInfo info) {
        mContext = context;
        mInfo = info;
    }

    @Resolve
    private void onResolved() {
        titleTxt.setText(mInfo.getTitle());
        captionTxt.setText(mInfo.getDescription());
        timeTxt.setText(mInfo.getTime());
        Glide.with(mContext).load(mInfo.getImageUrl()).into(imageView);
    }
}
