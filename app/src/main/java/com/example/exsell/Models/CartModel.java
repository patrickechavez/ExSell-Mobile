package com.example.exsell.Models;


import java.util.Date;

public class CartModel {


    private String ownerName, owner_id, remnantId, imageUrl, title, buyerId;
    private Integer quantity, maxQuantity;
    private Double price, total, subTotal;
    private Date timeStamp;


    public CartModel(){}

    public CartModel(String ownerName, String owner_id, String remnantId, String imageUrl, String title, String buyerId, Integer quantity, Integer maxQuantity, Double price, Double total, Double subTotal, Date timeStamp) {
        this.ownerName = ownerName;
        this.owner_id = owner_id;
        this.remnantId = remnantId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.buyerId = buyerId;
        this.quantity = quantity;
        this.maxQuantity = maxQuantity;
        this.price = price;
        this.total = total;
        this.subTotal = subTotal;
        this.timeStamp = timeStamp;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public String getRemnantId() {
        return remnantId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public Double getPrice() {
        return price;
    }

    public Double getTotal() {
        return total;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }
}