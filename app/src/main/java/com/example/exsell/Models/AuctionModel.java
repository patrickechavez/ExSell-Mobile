package com.example.exsell.Models;

import java.util.List;

public class AuctionModel {

    private List<String> auctionImageUrl;
    private String title, startTime, endTime, backStory, bounceBack, categoryId, description, meetup, userId, bidPrice, bidOwnerId;
    private Double startPrice;


    public AuctionModel() { }

    public AuctionModel(List<String> auctionImageUrl, String title, String startTime, String endTime, String backStory, String bounceBack, String categoryId, String description, String meetup, String userId, String bidPrice, String bidOwnerId, Double startPrice) {
        this.auctionImageUrl = auctionImageUrl;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.backStory = backStory;
        this.bounceBack = bounceBack;
        this.categoryId = categoryId;
        this.description = description;
        this.meetup = meetup;
        this.userId = userId;
        this.bidPrice = bidPrice;
        this.bidOwnerId = bidOwnerId;
        this.startPrice = startPrice;
    }

    public List<String> getAuctionImageUrl() {
        return auctionImageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
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

    public Double getStartPrice() {
        return startPrice;
    }
}