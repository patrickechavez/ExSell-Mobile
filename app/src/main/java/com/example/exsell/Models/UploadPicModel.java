package com.example.exsell.Models;


import android.net.Uri;

import java.net.URI;

public class UploadPicModel {

    private Uri upload_image;

    public UploadPicModel() {

    }

    public UploadPicModel(Uri upload_image) {
        this.upload_image = upload_image;
    }

    public Uri getUpload_image() {
        return upload_image;
    }

    public void setUpload_image(Uri upload_image) {
        this.upload_image = upload_image;
    }
}
