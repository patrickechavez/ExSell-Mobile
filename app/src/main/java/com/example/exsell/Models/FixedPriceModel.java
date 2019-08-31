package com.example.exsell.Models;

import java.util.List;

public class FixedPriceModel {

    private List<String> remnantsPicUrl;
    private String title, backStory, bounceBack,categoryId, description, meetup ,userId;
    private Double price;
    private Integer quantity;

    public FixedPriceModel(){}


    public FixedPriceModel(List<String> remnantsPicUrl, String title, String backStory, String bounceBack, String categoryId, String description, String meetup, String userId, Double price, int quantity) {
        this.remnantsPicUrl = remnantsPicUrl;
        this.title = title;
        this.backStory = backStory;
        this.bounceBack = bounceBack;
        this.categoryId = categoryId;
        this.description = description;
        this.meetup = meetup;
        this.userId = userId;
        this.price = price;
        this.quantity = quantity;
    }

    public List<String> getRemnantsPicUrl() {
        return remnantsPicUrl;
    }

    public String getTitle() {
        return title;
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

    public Double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
