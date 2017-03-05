package com.dbfall16.pblcloset.naveen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.dbfall16.pblcloset.R;
import java.util.ArrayList;

/**
 * Created by naveenkumar on 12/1/16.
 */
public class ItemsAdapter extends BaseAdapter {
    ArrayList<PostedItem> mData;
    Context mContext;
    int mResource;

    public ItemsAdapter(Context mContext, ArrayList<PostedItem> mData, int mResource) {
        //super(mContext, mResource, mData);
        this.mResource = mResource;
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(mResource,parent,false);
        ImageView itemImage = (ImageView)convertView.findViewById(R.id.item_image);
        TextView itemName = (TextView)convertView.findViewById(R.id.item_title);
        TextView itemPrice = (TextView)convertView.findViewById(R.id.item_price);

        PostedItem item = mData.get(position);
        Picasso.with(mContext).load(item.getPicture()).into(itemImage);
        itemName.setText(item.getName());
        itemPrice.setText("Price : $"+item.getPrice());

        itemImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        itemImage.setPadding(8, 8, 8, 8);

        return convertView;
    }
}