package com.dbfall16.pblcloset.naveen;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.dbfall16.pblcloset.R;

/**
 * Created by naveenkumar on 12/7/16.
 */
public class BuyItems extends AsyncTask<String, Void, String> {
    CheckOutActivity activity;
    public BuyItems(CheckOutActivity activity){
        this.activity = activity;
    }
    @Override
    protected String doInBackground(String... strings) {
        String urlString = strings[0];
        String itemsString = strings[1];
        String buyerId = strings[2];
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(urlString)
                    .header("buyerId",buyerId)
                    .post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), itemsString))
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String responseString = response.body().string();
            return responseString;
        }catch (Exception e){

        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        if (response!=null){
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status =jsonObject.getString("status");
                activity.onBuyItemsResponse(status);
            }catch (Exception e){

            }
        }
    }

    interface BuyItemsInterFace{
        void onBuyItemsResponse(String status);
    }
}
