package com.dbfall16.pblcloset.naveen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dbfall16.pblcloset.ui.activity.LoginActivity;
import com.dbfall16.pblcloset.ui.activity.MainActivity;
import com.dbfall16.pblcloset.utils.AppConstants;
import com.dbfall16.pblcloset.utils.PreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.dbfall16.pblcloset.R;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ListView cartListView;
    Button checkOut;
    TextView msgTxt;
    CartListAdapter cartListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setTitle("Cart");

        cartListView = (ListView)findViewById(R.id.cartList);
        checkOut = (Button)findViewById(R.id.cartCheckOut);
        msgTxt = (TextView)findViewById(R.id.messageTxt);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String cartItemsString = prefs.getString("cartItems","");
        ArrayList<PostedItem> cartItems = new Gson().fromJson(cartItemsString,new TypeToken<ArrayList<PostedItem>>(){}.getType());

        if (cartItems!=null && cartItems.size()>0){
            cartListView.setVisibility(View.VISIBLE);
            checkOut.setVisibility(View.VISIBLE);
            msgTxt.setVisibility(View.GONE);

            cartListAdapter = new CartListAdapter(this,R.layout.cartitem,cartItems);
            cartListView.setAdapter(cartListAdapter);
        }else {
            cartListView.setVisibility(View.GONE);
            checkOut.setVisibility(View.GONE);
            msgTxt.setVisibility(View.VISIBLE);
        }

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this,CheckOutActivity.class);
                startActivity(intent);
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
        Intent intent = new Intent(CartActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
