package com.dbfall16.pblcloset.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by viseshprasad on 12/1/16.
 */

public class Item extends BaseModel {
    @SerializedName("itemId")
    private String itemId;

    @SerializedName("categories")
    private ArrayList<Category> categories;

    @SerializedName("description")
    private String description;

    @SerializedName("color")
    private String color;

    @SerializedName("itemType")
    private String itemType;

    @SerializedName("size")
    private String size;

    @SerializedName("brand")
    private String brand;

    @SerializedName("picture")
    private String picture;

    @SerializedName("receivedDate")
    private String dateReceived;

    @SerializedName("donorId")
    private String donorId;

    @SerializedName("processed")
    private boolean processed;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(String dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
