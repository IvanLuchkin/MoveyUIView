package com.example.moveyuiview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InfiniteFeedInfo {

    @SerializedName("title")
    @Expose
    private String title;

    private Integer movieId;

    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    @SerializedName("caption")
    @Expose
    private String description;

    @SerializedName("time")
    @Expose
    private String time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }
}
