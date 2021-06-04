package com.example.moveyuiview;

import com.uwetrottmann.tmdb2.entities.BaseMovie;

import java.util.ArrayList;
import java.util.List;

public class CurrentContextHolder {

    private static CurrentContextHolder instance;
    private final List<BaseMovie> cachedSavedMovies = new ArrayList<>();
    private final List<BaseMovie> suggestionMoviesCache = new ArrayList<>();
    private Integer lastKnownMovieId;
    private boolean isFirstTimeOpenedSuggestions = true;

    public static CurrentContextHolder getInstance() {
        if (instance == null) {
            instance = new CurrentContextHolder();
        }
        return instance;
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

    public List<BaseMovie> getSuggestionMoviesCache() {
        return suggestionMoviesCache;
    }

    public boolean isIsFirstTimeOpenedSuggestions() {
        return isFirstTimeOpenedSuggestions;
    }

    public void setIsFirstTimeOpenedSuggestions(boolean isFirstTimeOpenedSuggestions) {
        this.isFirstTimeOpenedSuggestions = isFirstTimeOpenedSuggestions;
    }
}