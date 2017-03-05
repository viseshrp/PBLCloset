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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.dbfall16.pblcloset.R;

import com.dbfall16.pblcloset.ui.activity.LoginActivity;
import com.dbfall16.pblcloset.ui.activity.MainActivity;
import com.dbfall16.pblcloset.utils.AppConstants;
import com.dbfall16.pblcloset.utils.PreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        final PostedItem item = new Gson().fromJson(getIntent().getExtras().getString("Item"),PostedItem.class);

        getSupportActionBar().setTitle(item.getName());

        TextView title = (TextView)findViewById(R.id.itemtitleText);
        TextView price = (TextView)findViewById(R.id.itemPriceLabel);
        TextView description = (TextView)findViewById(R.id.itemDescription);
        ImageView itemMainImage = (ImageView)findViewById(R.id.itemMainImage);
        final Button addToCart = (Button)findViewById(R.id.itemCartButton);
        Button checkOut = (Button)findViewById(R.id.itemCheckOut);


        title.setText(item.getName());
        price.setText("Price : $"+item.getPrice());
        description.setText(item.getDescription());
        Picasso.with(this).load(item.getPicture()).into(itemMainImage);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemToCart(item);
            }
        });

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this,CheckOutActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuitems, menu);
        MenuItem item = menu.findItem(R.id.action_refresh);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                showCart();
                return true;
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
        Intent intent = new Intent(Main2Activity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    void showCart(){
        Intent intent = new Intent(Main2Activity.this,CartActivity.class);
        startActivity(intent);
    }

    void addItemToCart(PostedItem item){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String cartItemsString = prefs.getString("cartItems","");
        ArrayList<PostedItem> cartItems = new Gson().fromJson(cartItemsString,new TypeToken<ArrayList<PostedItem>>(){}.getType());
        if (cartItems!=null){
            boolean isItemFound = false;
            for (PostedItem postedItem: cartItems){
                if (postedItem.getItemId()==item.getItemId()){
                    isItemFound = true;
                    Toast.makeText(Main2Activity.this,"Item is already in the Cart",Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            if (!isItemFound){
                cartItems.add(item);
                Toast.makeText(Main2Activity.this," Item added to cart ",Toast.LENGTH_SHORT).show();
            }
        }else {
            cartItems = new ArrayList<>();
            cartItems.add(item);
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Main2Activity.this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cartItems",new Gson().toJson(cartItems));
        editor.commit();
    }
}
