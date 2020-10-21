package com.example.moveyuiview;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

@Layout(R.layout.load_more_item_view)
public class ItemView {

    @View(R.id.newstitleText)
    private TextView titleTxt;

    @View(R.id.newsCaptionText)
    private TextView captionTxt;

    @View(R.id.newsTimeText)
    private TextView timeTxt;

    @View(R.id.newsImageView)
    private ImageView imageView;

    private InfiniteFeedInfo mInfo;
    private Context mContext;

    public ItemView(Context context, InfiniteFeedInfo info) {
        mContext = context;
        mInfo = info;
    }

    @Resolve
    private void onResolved() {
        titleTxt.setText(mInfo.getTitle());
        captionTxt.setText(mInfo.getCaption());
        timeTxt.setText(mInfo.getTime());
        Glide.with(mContext).load(mInfo.getImageUrl()).into(imageView);
    }
}
