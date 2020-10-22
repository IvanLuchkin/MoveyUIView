package com.example.moveyuiview;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static final String TAG = "Utils";

    public static List<InfiniteFeedInfo> loadInfiniteFeeds(Context context){
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            JSONArray array = new JSONArray(loadJSONFromAsset(context));
            List<InfiniteFeedInfo> feedList = new ArrayList<>();
            for(int i = 0; i < array.length(); i++){
                InfiniteFeedInfo feed = gson.fromJson(array.getString(i), InfiniteFeedInfo.class);
                feedList.add(feed);
            }
            return feedList;
        }catch (Exception e){
            Log.d(TAG,"seedGames parseException " + e);
            e.printStackTrace();
            return null;
        }
    }

    private static String loadJSONFromAsset(Context context) {
        String json = null;
        InputStream is = null;
        try {
            AssetManager manager = context.getAssets();
            Log.d(TAG,"path "+ "news.json");
            is = manager.open("news.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
