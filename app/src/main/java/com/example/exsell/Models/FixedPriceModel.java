package com.example.exsell.Models;

import java.util.Date;
import java.util.List;

public class FixedPriceModel {

    private List<String> imageUrl;
    private String title, backStory, bounceBack,categoryId, description, meetup ,userId, type;
    private Double price;
    private Integer quantity;
    private Boolean isActive;
    private Date timeStamp;

    public FixedPriceModel(){}

    public FixedPriceModel(List<String> imageUrl, String title, String backStory, String bounceBack, String categoryId, String description, String meetup, String userId, String type, Double price, Integer quantity, Boolean isActive, Date timeStamp) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.backStory = backStory;
        this.bounceBack = bounceBack;
        this.categoryId = categoryId;
        this.description = description;
        this.meetup = meetup;
        this.userId = userId;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.isActive = isActive;
        this.timeStamp = timeStamp;
    }

    public List<String> getImageUrl() {
        return imageUrl;
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

    public String getType() {
        return type;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Boolean getActive() {
        return isActive;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }
}
