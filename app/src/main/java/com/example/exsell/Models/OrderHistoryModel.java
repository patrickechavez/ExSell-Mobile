package com.example.exsell.Models;

import java.util.Date;

public class OrderHistoryModel {

    private String remnantId;
    private Integer subTotal, quantity;
    private Date timeStamp;


    public OrderHistoryModel(){}

    public OrderHistoryModel(String remnantId, Integer subTotal, Integer quantity, Date timeStamp) {

        this.remnantId = remnantId;
        this.subTotal = subTotal;
        this.quantity = quantity;
        this.timeStamp = timeStamp;
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
