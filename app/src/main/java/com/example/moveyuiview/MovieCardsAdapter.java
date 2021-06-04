package com.example.moveyuiview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.uwetrottmann.tmdb2.entities.BaseMovie;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MovieCardsAdapter extends ArrayAdapter<BaseMovie> {

    public MovieCardsAdapter(Context context, int resourceID, List<BaseMovie> items) {
        super(context, resourceID, items);
    }

    public static void LoadImage(String url, ImageView imageView) {
        ImageViewHelper viewHelper = new ImageViewHelper();
        try {
            Bitmap bitmap = viewHelper.execute(url).get();
            imageView.setImageBitmap(bitmap);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        BaseMovie movieCardItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_card, parent, false);
        }
        TextView title = convertView.findViewById(R.id.title);
        ImageView image = convertView.findViewById(R.id.movieImageView);
        TextView releaseYear = convertView.findViewById(R.id.year);
        releaseYear.setText(movieCardItem.release_date == null ? "-" : movieCardItem.release_date.toString());
        LoadImage(movieCardItem.poster_path, image);
        title.setText(movieCardItem.title);
        return convertView;
    }

    public static class ImageViewHelper extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            URL url;
            HttpURLConnection httpURLConnection;
            InputStream inputStream;

            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                inputStream = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }

}
