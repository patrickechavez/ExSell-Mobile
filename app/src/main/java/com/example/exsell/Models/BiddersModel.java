package com.example.exsell.Models;

import java.util.Date;

public class BiddersModel {

    private Double bidAmount;
    private String userId;
    private String remnantId;
    private Date timeStamp;


    public BiddersModel() { }

    public BiddersModel(Double bidAmount, String userId, String remnantId, Date timeStamp) {
        this.bidAmount = bidAmount;
        this.userId = userId;
        this.remnantId = remnantId;
        this.timeStamp = timeStamp;
    }

    public Double getBidAmount() {
        return bidAmount;
    }

    public String getUserId() {
        return userId;
    }

    public String getRemnantId() {
        return remnantId;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }
}
