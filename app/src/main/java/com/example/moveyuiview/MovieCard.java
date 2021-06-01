package com.example.moveyuiview;

public class MovieCard {

    private String movieTitle;
    private String imageUrl;

    public MovieCard(String movieTitle,String imageUrl) {
        this.movieTitle = movieTitle;
        this.imageUrl = imageUrl;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
