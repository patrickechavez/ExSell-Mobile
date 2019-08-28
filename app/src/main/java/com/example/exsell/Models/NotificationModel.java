package com.example.exsell.Models;

import com.google.firebase.Timestamp;

import java.util.Date;

public class NotificationModel {

    private String user_id;
    private String message;
    private Date timeStamp;

    public NotificationModel(){}

    public NotificationModel(String user_id, String message, Date timeStamp) {
        this.user_id = user_id;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }


}
