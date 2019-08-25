package com.example.exsell.Models;

import java.util.List;

public class AuctionModel {

    private List<String> auctionImageUrl;
    private String title, startPrice, startTime, endTime, backStory, bounceBack,categoryId, description, meetup ,userId, bidPrice, bidOwnerId;

    public AuctionModel(){}

    public AuctionModel(List<String> auctionImageUrl, String title, String startPrice, String startTime, String endTime, String backStory, String bounceBack, String categoryId, String description, String meetup, String userId, String bidPrice, String bidOwnerId) {
        this.auctionImageUrl = auctionImageUrl;
        this.title = title;
        this.startPrice = startPrice;
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
    }

    public List<String> getAuctionImageUrl() {
        return auctionImageUrl;
    }

    public void setAuctionImageUrl(List<String> auctionImageUrl) {
        this.auctionImageUrl = auctionImageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(String startPrice) {
        this.startPrice = startPrice;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBackStory() {
        return backStory;
    }

    public void setBackStory(String backStory) {
        this.backStory = backStory;
    }

    public String getBounceBack() {
        return bounceBack;
    }

    public void setBounceBack(String bounceBack) {
        this.bounceBack = bounceBack;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMeetup() {
        return meetup;
    }

    public void setMeetup(String meetup) {
        this.meetup = meetup;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(String bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getBidOwnerId() {
        return bidOwnerId;
    }

    public void setBidOwnerId(String bidOwnerId) {
        this.bidOwnerId = bidOwnerId;
    }
}
