package com.example.exsell.Models;

public class ListRemnants1Model {

    private String title, description, backStory, bounceBackPlan;


    public ListRemnants1Model(){

    }

    public ListRemnants1Model(String title, String description, String backStory, String bounceBackPlan) {
        this.title = title;
        this.description = description;
        this.backStory = backStory;
        this.bounceBackPlan = bounceBackPlan;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackStory() {
        return backStory;
    }

    public void setBackStory(String backStory) {
        this.backStory = backStory;
    }

    public String getBounceBackPlan() {
        return bounceBackPlan;
    }

    public void setBounceBackPlan(String bounceBackPlan) {
        this.bounceBackPlan = bounceBackPlan;
    }
}
