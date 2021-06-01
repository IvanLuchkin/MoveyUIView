package com.example.moveyuiview;

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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
        LoadImage(movieCardItem.getImageUrl(),image);
        title.setText(movieCardItem.getMovieTitle());
        return convertView;
    }

    public static void LoadImage(String url, ImageView imageView){
        ImageViewHelper viewHelper = new ImageViewHelper();
        try {
            Bitmap bitmap = viewHelper.execute(url).get();
            imageView.setImageBitmap(bitmap);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static class ImageViewHelper extends AsyncTask<String,Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            URL url ;
            HttpURLConnection httpURLConnection;
            InputStream inputStream;

            try{
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                inputStream=httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);

            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  bitmap;
        }
    }

}
