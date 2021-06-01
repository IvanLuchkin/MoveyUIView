package com.example.moveyuiview;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MovieCardsAdapter extends ArrayAdapter<MovieCard> {

    Context context;
    Bitmap bitmap;

    public MovieCardsAdapter(Context context, int resourceID, List<MovieCard> items) {
        super(context, resourceID, items);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        MovieCard movieCardItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_card, parent, false);
        }
        TextView title = convertView.findViewById(R.id.title);
        ImageView image = convertView.findViewById(R.id.movieImageView);
        SuggestionsActivity.LoadImage(movieCardItem.getImageUrl(),image);
        title.setText(movieCardItem.getMovieTitle());
        image.setImageResource(R.mipmap.ic_launcher);
        return convertView;
    }
}
