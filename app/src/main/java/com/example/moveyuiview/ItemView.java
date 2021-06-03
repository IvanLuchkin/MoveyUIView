package com.example.moveyuiview;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

@Layout(R.layout.load_more_item_view)
public class ItemView {

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

    public ItemView(Context context, InfiniteFeedInfo info) {
        mContext = context;
        mInfo = info;
    }

    @Resolve
    private void onResolved() {
        titleTxt.setText(mInfo.getTitle());
        captionTxt.setText(mInfo.getDescription());
        timeTxt.setText(mInfo.getTime());
        imageView.setClickable(true);
        imageView.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                CurrentContextHolder.getInstance().setLastKnownMovieId(mInfo.getMovieId());
                Intent intent =new Intent(mContext,ReviewsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        Glide.with(mContext).load(mInfo.getImageUrl()).into(imageView);
    }
}
