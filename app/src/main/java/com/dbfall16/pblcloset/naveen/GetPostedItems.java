package com.dbfall16.pblcloset.naveen;

import android.os.AsyncTask;

import com.dbfall16.pblcloset.ui.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by naveenkumar on 12/7/16.
 */
public class GetPostedItems extends AsyncTask<String,Void,String> {
    MainActivity activity;
    public GetPostedItems(MainActivity activity) {
        this.activity = activity;
    }
    @Override
    protected String doInBackground(String... strings) {
        String urlString = strings[0];
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(urlString)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String responseString = response.body().string();
            return responseString;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        ArrayList<PostedItem> items = parsePostedItems(response);
        activity.onPostedItemsResponse(items);
    }

    private ArrayList<PostedItem> parsePostedItems(String response){
        if (response!=null){
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status =jsonObject.getString("status");
                if (status.equals("success")){
                    JSONArray itemslist = jsonObject.getJSONArray("response");
                    ArrayList<PostedItem> items = new ArrayList<>();
                    for (int i=0; i<itemslist.length(); i++){
                        JSONObject item = itemslist.getJSONObject(i);
                        int id = item.getInt("itemId");
                        String name = item.getString("name");
                        String description = item.getString("description");
                        String color = item.getString("color");
                        String itemType = item.getString("itemType");
                        String size = item.getString("size");
                        String brand = item.getString("brand");
                        String picture = item.getString("picture");
                        int processedFlag = item.getInt("itemId");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
                        Date receivedDate = dateFormat.parse(item.getString("receivedDate"));
                        int donorId = item.getInt("donorId");
                        double price = item.getDouble("price");
                        JSONArray categoriesList = item.getJSONArray("categories");
                        List<Category> categories = new ArrayList<>();
                        for (int j=0;j<categoriesList.length();j++){
                            JSONObject categoryItem = categoriesList.getJSONObject(j);

                            int categId = categoryItem.getInt("categId");
                            int itemId = categoryItem.getInt("itemId");
                            String categDescription = categoryItem.getString("categDescription");
                            String categGender = categoryItem.getString("categGender");

                            Category category = new Category();
                            category.setCategId(categId);
                            category.setItemId(itemId);
                            category.setCategDescription(categDescription);
                            category.setCategGender(categGender);
                            categories.add(category);
                        }
                        PostedItem postedItem = new PostedItem();
                        postedItem.setItemId(id);
                        postedItem.setName(name);
                        postedItem.setCategories(categories);
                        postedItem.setDescription(description);
                        postedItem.setColor(color);
                        postedItem.setItemType(itemType);
                        postedItem.setSize(size);
                        postedItem.setBrand(brand);
                        postedItem.setPicture(picture);
                        postedItem.setProcessedFlag(processedFlag);
                        postedItem.setReceivedDate(receivedDate);
                        postedItem.setDonorId(donorId);
                        postedItem.setPrice(price);
                        items.add(postedItem);
                    }
                    return items;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public interface PostedItemsInterface{
        void onPostedItemsResponse(ArrayList<PostedItem> items);
    }
}