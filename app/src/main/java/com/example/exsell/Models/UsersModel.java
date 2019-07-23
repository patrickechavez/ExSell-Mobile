package com.example.exsell.Models;

public class UsersModel {

    private String user_id;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String online;

    public UsersModel(){}

    public UsersModel(String user_id, String firstName, String lastName, String imageUrl, String online) {
        this.user_id = user_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUrl = imageUrl;
        this.online = online;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getOnline() {
        return online;
    }
}
