package com.dbfall16.pblcloset.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by viseshprasad on 12/1/16.
 */

public class ItemList extends BaseModel {
    @SerializedName("response")
    ArrayList<Item> itemList;

    public ArrayList<Item> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<Item> itemList) {
        this.itemList = itemList;
    }
}
