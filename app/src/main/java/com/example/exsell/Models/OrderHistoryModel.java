package com.example.exsell.Models;

import java.util.Date;

public class OrderHistoryModel {

    private String ownerId, remnantId;
    private Integer subTotal, quantity;
    private Date timeStamp;


    public OrderHistoryModel(){}

    public OrderHistoryModel(String ownerId, String remnantId, Integer subTotal, Integer quantity, Date timeStamp) {
        this.ownerId = ownerId;
        this.remnantId = remnantId;
        this.subTotal = subTotal;
        this.quantity = quantity;
        this.timeStamp = timeStamp;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getRemnantId() {
        return remnantId;
    }

    public Integer getSubTotal() {
        return subTotal;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }
}
