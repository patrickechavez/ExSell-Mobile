package com.example.exsell.Models;


import android.net.Uri;

import java.net.URI;

public class UploadPicModel {

    private Uri upload_image;
    private String imageUrl;

    public UploadPicModel() {

    }

    public UploadPicModel(Uri upload_image, String imageUrl) {
        this.upload_image = upload_image;
        this.imageUrl = imageUrl;
    }

    public Uri getUpload_image() {
        return upload_image;
    }

    public void setUpload_image(Uri upload_image) {
        this.upload_image = upload_image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /*public UploadPicModel(Uri upload_image) {
        this.upload_image = upload_image;
    }

    public Uri getUpload_image() {
        return upload_image;
    }

    public void setUpload_image(Uri upload_image) {
        this.upload_image = upload_image;
    }*/
}
