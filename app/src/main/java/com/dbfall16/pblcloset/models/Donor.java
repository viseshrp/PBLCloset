package com.dbfall16.pblcloset.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by viseshprasad on 12/1/16.
 */

public class Donor extends User {
    @SerializedName("donationsMade")
    private String donationCount;

    public String getDonationCount() {
        return donationCount;
    }

    public void setDonationCount(String donationCount) {
        this.donationCount = donationCount;
    }
}
