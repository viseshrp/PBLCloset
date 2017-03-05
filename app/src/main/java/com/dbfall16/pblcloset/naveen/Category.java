package com.dbfall16.pblcloset.naveen;

/**
 * Created by kishorekolluru on 12/1/16.
 */
public class Category {
    private int categId;
    private int itemId;
    private String categDescription;
    private String categGender;

    public int getCategId() {
        return categId;
    }

    public void setCategId(int categId) {
        this.categId = categId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getCategDescription() {
        return categDescription;
    }

    public void setCategDescription(String categDescription) {
        this.categDescription = categDescription;
    }

    public String getCategGender() {
        return categGender;
    }

    public void setCategGender(String categGender) {
        this.categGender = categGender;
    }
}
