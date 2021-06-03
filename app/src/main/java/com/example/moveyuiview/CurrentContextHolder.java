package com.example.moveyuiview;

import com.uwetrottmann.tmdb2.entities.BaseMovie;

import java.util.ArrayList;
import java.util.List;

public class CurrentContextHolder {

    private static CurrentContextHolder instance;
    private Integer lastKnownMovieId;
    private List<BaseMovie> cachedSavedMovies = new ArrayList<>();

    private CurrentContextHolder(){}


    public static CurrentContextHolder getInstance(){
        if(instance == null){
            instance = new CurrentContextHolder();
            return instance;
        }else return instance;
    }

    public void showMessage(){
        System.out.println("Hello World!");
    }



    public Integer getLastKnownMovieId() {
        return lastKnownMovieId;
    }

    public void setLastKnownMovieId(Integer lastKnownMovieId) {
        this.lastKnownMovieId = lastKnownMovieId;
    }

    public List<BaseMovie> getCachedSavedMovies() {
        return cachedSavedMovies;
    }

    public void setCachedSavedMovies(List<BaseMovie> cachedSavedMovies) {
        this.cachedSavedMovies = cachedSavedMovies;
    }
}