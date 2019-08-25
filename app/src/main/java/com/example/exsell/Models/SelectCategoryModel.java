package com.example.exsell.Models;

import android.widget.ImageView;

public class SelectCategoryModel {

    private String categoryId;
    private String categoryName;
    private String categoryImageUrl;



    public SelectCategoryModel(){

    }

    public SelectCategoryModel(String categoryId, String categoryName, String categoryImageUrl) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryImageUrl = categoryImageUrl;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryImageUrl() {
        return categoryImageUrl;
    }
}
