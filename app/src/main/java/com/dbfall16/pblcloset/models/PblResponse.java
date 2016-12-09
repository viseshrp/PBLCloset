package com.dbfall16.pblcloset.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by viseshprasad on 12/8/16.
 */

public class PblResponse extends BaseModel{


    @SerializedName("status")
    private String status;

    @SerializedName("error")
    private String error;

    @SerializedName("response")
    private Object response;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}
