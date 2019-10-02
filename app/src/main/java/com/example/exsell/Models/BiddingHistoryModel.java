package com.example.exsell.Models;

import java.util.Date;

public class BiddingHistoryModel {

    private String remnantId, userId;
    private Date timeStamp;
    private Double bidAmount;

    public BiddingHistoryModel() {
    }

    public BiddingHistoryModel(String remnantId, String userId, Date timeStamp, Double bidAmount) {
        this.remnantId = remnantId;
        this.userId = userId;
        this.timeStamp = timeStamp;
        this.bidAmount = bidAmount;
    }

    public String getRemnantId() {
        return remnantId;
    }

    public String getUserId() {
        return userId;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public Double getBidAmount() {
        return bidAmount;
    }


}
