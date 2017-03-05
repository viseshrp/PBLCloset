package com.dbfall16.pblcloset.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dbfall16.pblcloset.R;
import com.dbfall16.pblcloset.naveen.CheckOutActivity;
import com.dbfall16.pblcloset.naveen.PostedItem;
import com.dbfall16.pblcloset.naveen.CartActivity;
import com.dbfall16.pblcloset.naveen.GetPostedItems;
import com.dbfall16.pblcloset.naveen.ItemsAdapter;
import com.dbfall16.pblcloset.naveen.Main2Activity;
import com.dbfall16.pblcloset.utils.APIConstants;
import com.dbfall16.pblcloset.utils.AppConstants;
import com.dbfall16.pblcloset.utils.PreferencesUtils;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetPostedItems.PostedItemsInterface{

    ArrayList<PostedItem> items = new ArrayList<>();
    GridView itemsGrid;
    ArrayList<String> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Home");
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString("cartItems","");
//        editor.commit();

        TextView welcomeText = (TextView) findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome, "+ PreferencesUtils.getString(this, AppConstants.USER_FIRST_NAME,"") +" "+PreferencesUtils.getString(this, AppConstants.USER_LAST_NAME,""));

        itemsGrid = (GridView)findViewById(R.id.itemsGrid);

        ItemsAdapter itemsAdapter = new ItemsAdapter(this,items,R.layout.itemlayout);
        itemsGrid.setAdapter(itemsAdapter);

        itemsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PostedItem item = items.get(i);
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                intent.putExtra("Item",new Gson().toJson(item));
                startActivity(intent);
            }
        });
        refresh();
        ImageView filterButton = (ImageView)findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterOptions();
            }
        });
        TextView filterText = (TextView)findViewById(R.id.filterText);
        filterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterOptions();
            }
        });
        refresh();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuitems, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                showCart();
                return true;
            case R.id.action_refresh:
                refresh();
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
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    void showCart(){
        Intent intent = new Intent(MainActivity.this,CartActivity.class);
        startActivity(intent);
    }

    void refresh(){
        new GetPostedItems(this).execute(APIConstants.BASE_URL+"/PBLCloset/allPostedItems");
    }

    void filterItems(){

    }

    void showFilterOptions(){
        final String[] categoryItems = {"Men", "Women", "Boys", "Girls", "Kids"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        categories.clear();
        builder.setTitle("Select Category")
                .setItems(categoryItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        categories.add(categoryItems[item]);
                    }
                }).setPositiveButton("Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                filterItems();
            }
        });

        builder.create().show();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onPostedItemsResponse(ArrayList<com.dbfall16.pblcloset.naveen.PostedItem> itemsList) {
        if (itemsList!=null) {
            this.items = itemsList;
            ItemsAdapter itemsAdapter = new ItemsAdapter(this, items, R.layout.itemlayout);
            itemsGrid.setAdapter(itemsAdapter);
        }
    }
}
