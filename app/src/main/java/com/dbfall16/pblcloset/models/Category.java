package com.dbfall16.pblcloset.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by viseshprasad on 12/1/16.
 */

public class Category extends BaseModel {

    @SerializedName("categId")
    private String categId;

    @SerializedName("itemId")
    private String itemId;

    @SerializedName("categGender")
    private String categGender;

    @SerializedName("categDescription")
    private String categDescription;

    public String getCategId() {
        return categId;
    }

    public void setCategId(String categId) {
        this.categId = categId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCategGender() {
        return categGender;
    }

    public void setCategGender(String categGender) {
        this.categGender = categGender;
    }

    public String getCategDescription() {
        return categDescription;
    }

    public void setCategDescription(String categDescription) {
        this.categDescription = categDescription;
    }
}
