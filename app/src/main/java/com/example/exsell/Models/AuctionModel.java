package com.example.exsell.Models;

import java.util.List;

public class AuctionModel {

    private List<String> imageUrl;
    private String title, startTime, backStory, bounceBack, categoryId, description, meetup, userId, bidPrice, bidOwnerId;
    private Double price;
    private Long endTime, idleDuration;

    public AuctionModel() { }

    public AuctionModel(List<String> imageUrl, String title, String startTime, String backStory, String bounceBack, String categoryId, String description, String meetup, String userId, String bidPrice, String bidOwnerId, Double price, Long endTime, Long idleDuration) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.startTime = startTime;
        this.backStory = backStory;
        this.bounceBack = bounceBack;
        this.categoryId = categoryId;
        this.description = description;
        this.meetup = meetup;
        this.userId = userId;
        this.bidPrice = bidPrice;
        this.bidOwnerId = bidOwnerId;
        this.price = price;
        this.endTime = endTime;
        this.idleDuration = idleDuration;
    }

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getStartTime() {
        return startTime;
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

    public String getBidPrice() {
        return bidPrice;
    }

    public String getBidOwnerId() {
        return bidOwnerId;
    }

    public Double getPrice() {
        return price;
    }

    public Long getEndTime() {
        return endTime;
    }

    public Long getIdleDuration() {
        return idleDuration;
    }
}