package com.example.exsell.Models;

import java.util.List;

public class BlogModel {

    public List<String> imageUrl;
    public String title, breakupStage, gist, juice;

    public BlogModel(){}

    public BlogModel(List<String> imageUrl, String title, String breakupStage, String gist, String juice) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.breakupStage = breakupStage;
        this.gist = gist;
        this.juice = juice;
    }

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getBreakupStage() {
        return breakupStage;
    }

    public String getGist() {
        return gist;
    }

    public String getJuice() {
        return juice;
    }
}
