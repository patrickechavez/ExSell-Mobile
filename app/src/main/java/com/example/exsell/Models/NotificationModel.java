package com.example.exsell.Models;

import android.widget.ImageView;

import com.google.firebase.Timestamp;

import java.util.Date;

public class NotificationModel {

    private String receiver_id, sender_id, notificationType, message, message2, imageUrl, remnants_id;
    private Date timeStamp;


    public NotificationModel(){}

    public NotificationModel(String receiver_id, String sender_id, String notificationType, String message, String message2, String imageUrl, String remnants_id, Date timeStamp) {
        this.receiver_id = receiver_id;
        this.sender_id = sender_id;
        this.notificationType = notificationType;
        this.message = message;
        this.message2 = message2;
        this.imageUrl = imageUrl;
        this.remnants_id = remnants_id;
        this.timeStamp = timeStamp;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage2() {
        return message2;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getRemnants_id() {
        return remnants_id;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

}
