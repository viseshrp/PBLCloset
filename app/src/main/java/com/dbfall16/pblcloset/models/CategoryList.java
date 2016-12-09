package com.dbfall16.pblcloset.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by viseshprasad on 12/1/16.
 */

public class CategoryList extends BaseModel {
    @SerializedName("categories")
    ArrayList<Category> categoryList;

    public ArrayList<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<Category> categoryList) {
        this.categoryList = categoryList;
    }
}
