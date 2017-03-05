package com.dbfall16.pblcloset.naveen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.dbfall16.pblcloset.R;

import java.util.List;

/**
 * Created by naveenkumar on 12/7/16.
 */
public class CartListAdapter extends ArrayAdapter<PostedItem> {
    Context mContext;
    int mResource;
    List<PostedItem> mData;
    public CartListAdapter(Context context, int resource, List<PostedItem> feeds) {
        super(context, resource,feeds);
        this.mContext = context;
        this.mResource = resource;
        this.mData = feeds;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);
        }

        ImageView itemImage = (ImageView)convertView.findViewById(R.id.cItemImage);
        TextView itemName = (TextView)convertView.findViewById(R.id.cItemName);
        TextView itemPrice = (TextView)convertView.findViewById(R.id.cItemPrice);

        PostedItem item = mData.get(position);

        Picasso.with(mContext).load(item.getPicture()).into(itemImage);
        itemName.setText(item.getName());
        itemPrice.setText("Price: $"+item.getPrice());

        return convertView;
    }

}
