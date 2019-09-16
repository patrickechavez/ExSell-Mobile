package com.example.exsell.Models;

import java.util.Date;

public class BiddersModel {

    private Double bidAmount;
    private String userId;
    private Date timeStamp;


    public BiddersModel() { }

    public BiddersModel(Double bidAmount, String userId, Date timeStamp) {
        this.bidAmount = bidAmount;
        this.userId = userId;
        this.timeStamp = timeStamp;
    }

    public Double getBidAmount() {
        return bidAmount;
    }

    public String getUserId() {
        return userId;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }
}
