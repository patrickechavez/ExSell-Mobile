package com.example.exsell.Models;


import java.util.Date;

public class CartModel {


    private String buyerId, remnantId;
    private Date timeStamp;
    private Integer quantity;
    private Double subTotal;

    public CartModel(){}

    public CartModel(String buyerId, String remnantId, Date timeStamp, Integer quantity, Double subTotal) {
        this.buyerId = buyerId;
        this.remnantId = remnantId;
        this.timeStamp = timeStamp;
        this.quantity = quantity;
        this.subTotal = subTotal;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public String getRemnantId() {
        return remnantId;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getSubTotal() {
        return subTotal;
    }
}