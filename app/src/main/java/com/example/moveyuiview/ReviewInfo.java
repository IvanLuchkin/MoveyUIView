package com.example.moveyuiview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewInfo {
    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("date")
    @Expose
    private String date;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
