package com.example.exsell.Models;

import java.util.List;

public class FixedPriceModel {

    private List<String> remnantsPicUrl;
    private String title, price, quantity, backStory, bounceBack,categoryId, description, meetup ,userId;


    public FixedPriceModel(){}


    public FixedPriceModel(List<String> remnantsPicUrl, String title, String price, String quantity, String backStory, String bounceBack, String categoryId, String description, String meetup, String owner_id) {
        this.remnantsPicUrl = remnantsPicUrl;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.backStory = backStory;
        this.bounceBack = bounceBack;
        this.categoryId = categoryId;
        this.description = description;
        this.meetup = meetup;
        this.userId = userId;
    }

    public List<String> getRemnantsPicUrl() {
        return remnantsPicUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getBackStory() {
        return backStory;
    }

    public String getBounceBack() {
        return bounceBack;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getDescription() {
        return description;
    }

    public String getMeetup() {
        return meetup;
    }

    public String getUserId() {
        return userId;
    }
}
