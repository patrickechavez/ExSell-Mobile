package com.example.exsell.Models;

import java.util.Date;

public class MessageModel {

    private String message, type, sender, receiver;
    private Date time;
    private boolean seen;

   public MessageModel(){

    }

    public MessageModel(String message, String type, String sender, String receiver, Date time) {
        this.message = message;
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.time = time;
        this.seen = seen;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public Date getTime() {
        return time;
    }

    public boolean isSeen() {
        return seen;
    }
}
