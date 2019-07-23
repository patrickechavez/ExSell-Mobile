package com.example.exsell.Models;

public class SelectCategoryModel {

    private String categoryName;


    public SelectCategoryModel(){

    }

    public SelectCategoryModel(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
