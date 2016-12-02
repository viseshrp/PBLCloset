package com.dbfall16.pblcloset.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by viseshprasad on 12/1/16.
 */

public class PostedItemList extends BaseModel {

    @SerializedName("postedItemList")
    ArrayList<PostedItem> postedItemList;

    public ArrayList<PostedItem> getPostedItemList() {
        return postedItemList;
    }

    public void setPostedItemList(ArrayList<PostedItem> postedItemList) {
        this.postedItemList = postedItemList;
    }
}
