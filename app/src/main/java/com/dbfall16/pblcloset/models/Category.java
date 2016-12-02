package com.dbfall16.pblcloset.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by viseshprasad on 12/1/16.
 */

public class Category extends BaseModel {

    @SerializedName("categoryId")
    private String categoryId;


    @SerializedName("categoryName")
    private String categoryName;


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
