package com.example.exsell.Models;

import java.util.Date;

public class CommentModel {

    private String comment, userId, blogId;
    private Date timeStamp;

    public CommentModel(){}


    public CommentModel(String comment, String userId, String blogId, Date timeStamp) {
        this.comment = comment;
        this.userId = userId;
        this.blogId = blogId;
        this.timeStamp = timeStamp;
    }

    public String getComment() {
        return comment;
    }

    public String getUserId() {
        return userId;
    }

    public String getBlogId() {
        return blogId;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }
}
