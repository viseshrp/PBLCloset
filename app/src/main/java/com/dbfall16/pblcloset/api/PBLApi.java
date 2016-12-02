package com.dbfall16.pblcloset.api;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.dbfall16.pblcloset.models.User;

/**
 * Created by viseshprasad on 11/14/16.
 */

public class PBLApi {
    private final RequestQueue mRequestQueue;

    public PBLApi(RequestQueue mRequestQueue) {
        this.mRequestQueue = mRequestQueue;
    }


    public Request<?> login(String userType, String email, String password, Response.Listener<User> listener,
                            Response.ErrorListener errorListener) {
        return mRequestQueue.add(new LoginApi(userType, email, password, listener, errorListener));
    }

}