package com.example.exsell.Models;

import com.google.firebase.Timestamp;

import java.util.Date;

public class NotificationModel {

    private String receiver_id;
    private String sender_id;
    private String notificationType;
    private String message;
    private Date timeStamp;

    public NotificationModel(){}

    public NotificationModel(String receiver_id, String sender_id, String notificationType, String message, Date timeStamp) {
        this.receiver_id = receiver_id;
        this.sender_id = sender_id;
        this.notificationType = notificationType;
        this.message = message;
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

    public Date getTimeStamp() {
        return timeStamp;
    }


}
