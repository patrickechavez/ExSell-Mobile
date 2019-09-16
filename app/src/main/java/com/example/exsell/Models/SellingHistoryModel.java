package com.example.exsell.Models;

import java.util.Date;
import java.util.List;

public class SellingHistoryModel {

    private List<String> imageUrl;
    private String title, type;
    private Integer quantity;
    private Double price, bidAmount;
    private Date timeStamp;
    private Long endTime;

    public SellingHistoryModel(){}

    public SellingHistoryModel(List<String> imageUrl, String title, String type, Integer quantity, Double price, Double bidAmount, Date timeStamp, Long endTime) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.bidAmount = bidAmount;
        this.timeStamp = timeStamp;
        this.endTime = endTime;
    }

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public Double getBidAmount() {
        return bidAmount;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public Long getEndTime() {
        return endTime;
    }
}
