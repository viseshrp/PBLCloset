package com.dbfall16.pblcloset;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.RequestTickle;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.VolleyTickle;
import com.dbfall16.pblcloset.api.PBLApi;
import com.dbfall16.pblcloset.utils.HttpManager;

/**
 * Created by viseshprasad on 11/14/16.
 */

public class PBLApp extends Application {

    private static PBLApp pblApp;
    private RequestQueue mRequestQueue;
    private RequestTickle mRequestTickle;
    private Context mContext;

    private PBLApi pblApi;

    public static PBLApp get() {
        if (pblApp == null) {
            System.exit(0);
        }
        return pblApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        pblApp = this;

        setupNetwork();
        setupApi();
    }

    public PBLApi getPblApi() {
        return pblApi;
    }

    private void setupNetwork() {
        HttpManager httpManager = new HttpManager(this);
        HttpStack httpStack = httpManager.getHttpStack();

        mRequestQueue = Volley.newRequestQueue(this);
        mRequestTickle = VolleyTickle.newRequestTickle(this, httpStack);
    }

    private void setupApi() {
        pblApi = new PBLApi(mRequestQueue);
    }

}
