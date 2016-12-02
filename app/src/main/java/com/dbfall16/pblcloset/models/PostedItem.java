package com.dbfall16.pblcloset.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by viseshprasad on 12/1/16.
 */

public class PostedItem extends Item {
    @SerializedName("postedDate")
    private String postedDate;

    @SerializedName("price")
    private String price;

    @SerializedName("discount")
    private boolean discount;

    @SerializedName("brandValue")
    private String brandValue;

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isDiscount() {
        return discount;
    }

    public void setDiscount(boolean discount) {
        this.discount = discount;
    }

    public String getBrandValue() {
        return brandValue;
    }

    public void setBrandValue(String brandValue) {
        this.brandValue = brandValue;
    }
}
