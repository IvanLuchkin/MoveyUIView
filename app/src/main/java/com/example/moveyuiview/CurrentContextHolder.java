package com.example.moveyuiview;

import java.util.ArrayList;
import java.util.List;

public class CurrentContextHolder {

    private static CurrentContextHolder instance;
    private List<InfiniteFeedInfo> feedList;
    private Integer lastKnownMovieId;

    private CurrentContextHolder(){}


    public static CurrentContextHolder getInstance(){
        if(instance == null){
            instance = new CurrentContextHolder();
            instance.feedList = new ArrayList<>();
            return instance;
        }else return instance;
    }

    public void showMessage(){
        System.out.println("Hello World!");
    }


    public List<InfiniteFeedInfo> getFeedList() {
        return feedList;
    }

    public void setFeedList(List<InfiniteFeedInfo> feedList) {
        this.feedList = feedList;
    }

    public Integer getLastKnownMovieId() {
        return lastKnownMovieId;
    }

    public void setLastKnownMovieId(Integer lastKnownMovieId) {
        this.lastKnownMovieId = lastKnownMovieId;
    }
}