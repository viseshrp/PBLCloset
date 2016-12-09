package com.dbfall16.pblcloset.api;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.dbfall16.pblcloset.models.Item;
import com.dbfall16.pblcloset.models.ItemList;
import com.dbfall16.pblcloset.models.PblResponse;
import com.dbfall16.pblcloset.models.User;

/**
 * Created by viseshprasad on 11/14/16.
 */

public class PBLApi {
    private final RequestQueue mRequestQueue;

    public PBLApi(RequestQueue mRequestQueue) {
        this.mRequestQueue = mRequestQueue;
    }


    public Request<?> login(String email, String password, Response.Listener<User> listener,
                            Response.ErrorListener errorListener) {
        return mRequestQueue.add(new LoginApi(email, password, listener, errorListener));
    }

    public Request<?> signup(String userType, String firstName, String lastName, String address, String city, String state,
                             String zip, String phone, String country, String dob, String email, String password, boolean subscription, Response.Listener<PblResponse> listener,
                             Response.ErrorListener errorListener) {
        return mRequestQueue.add(new SignupAPI(userType, firstName, lastName, address, city, state, zip, phone, country, dob, email, password,
                subscription, listener, errorListener));
    }

    public Request<?> getDonatedList(String userId, Response.Listener<ItemList> listener,
                                     Response.ErrorListener errorListener) {
        return mRequestQueue.add(new DonatedListApi(userId, listener, errorListener));
    }

    public Request<?> donate(String userId, String description, String color, String itemType, String size,
                             String brand, String picture, Response.Listener<Item> listener,
                             Response.ErrorListener errorListener) {
        return mRequestQueue.add(new DonateItemApi(userId, description, color, itemType, size, brand, picture, listener, errorListener));
    }


}
