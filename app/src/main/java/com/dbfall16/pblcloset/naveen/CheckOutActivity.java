package com.dbfall16.pblcloset.naveen;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dbfall16.pblcloset.R;
import com.dbfall16.pblcloset.ui.activity.LoginActivity;
import com.dbfall16.pblcloset.ui.activity.MainActivity;
import com.dbfall16.pblcloset.utils.APIConstants;
import com.dbfall16.pblcloset.utils.AppConstants;
import com.dbfall16.pblcloset.utils.PreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class CheckOutActivity extends AppCompatActivity implements BuyItems.BuyItemsInterFace{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        getSupportActionBar().setTitle("Check Out");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String cartItemsString = prefs.getString("cartItems","");
        ArrayList<PostedItem> cartItems = new Gson().fromJson(cartItemsString,new TypeToken<ArrayList<PostedItem>>(){}.getType());

        TextView noOfItems = (TextView)findViewById(R.id.noOfItems);
        TextView totalPrice = (TextView)findViewById(R.id.totalPrice);
        double price = 0.0;
        int items = 0;
        if (cartItems!=null && cartItems.size()>0) {
            for (PostedItem postedItem : cartItems) {
                items += 1;
                price += postedItem.getPrice();
            }
        }

        noOfItems.setText(items+"");
        totalPrice.setText(price+"");

        Button payment = (Button)findViewById(R.id.paymentButton);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String buyerID = PreferencesUtils.getString(CheckOutActivity.this, AppConstants.USER_ID,"");
                int uID = (int) Double.parseDouble(buyerID);
                new BuyItems(CheckOutActivity.this).execute(APIConstants.BASE_URL+"/PBLCloset/itemBought",cartItemsString, uID+"");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuitems, menu);
        MenuItem item = menu.findItem(R.id.action_refresh);
        MenuItem item1 = menu.findItem(R.id.action_favorite);
        item.setVisible(false);
        item1.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void signOut(){
        PreferencesUtils.setString(this, AppConstants.USER_ID, "");
        PreferencesUtils.setString(this, AppConstants.EMAIL, "");
        PreferencesUtils.setString(this, AppConstants.USER_FIRST_NAME, "");
        PreferencesUtils.setString(this, AppConstants.USER_LAST_NAME, "");
        PreferencesUtils.setBoolean(this, AppConstants.IS_LOGGED_IN, false);
        Intent intent = new Intent(CheckOutActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBuyItemsResponse(String status) {
        if (status.equals("success")){
            AlertDialog.Builder builder = new AlertDialog.Builder(CheckOutActivity.this);
            builder.setTitle("Booking Message").setMessage("Items bought Successfully").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CheckOutActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("cartItems","");
                    editor.commit();
                    Intent intent = new Intent(CheckOutActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else {

        }
    }
}
