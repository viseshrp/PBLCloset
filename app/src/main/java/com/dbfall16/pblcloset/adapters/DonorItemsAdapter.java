package com.dbfall16.pblcloset.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dbfall16.pblcloset.R;
import com.dbfall16.pblcloset.models.Item;
import com.dbfall16.pblcloset.utils.AppConstants;

import java.util.ArrayList;

/**
 * Created by viseshprasad on 12/8/16.
 */

public class DonorItemsAdapter extends RecyclerView.Adapter<DonorItemsAdapter.ViewHolder> {
    private final ItemTapListener mItemTapListener;
    private ArrayList<Item> itemArrayList;
    private Context mContext;

    public DonorItemsAdapter(Context mContext, ItemTapListener listener) {
        itemArrayList = new ArrayList<>();
        mItemTapListener = listener;
        this.mContext = mContext;
    }

    public void setDataset(ArrayList<Item> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Item item = itemArrayList.get(position);

        holder.description.setText(item.getDescription());

        if (item.isProcessed())
            holder.status.setText(AppConstants.PROCESSED);
        else
            holder.status.setText(AppConstants.PENDING);

        holder.date.setText(item.getDateReceived());

        holder.wrapLayout.setId(position);
        holder.wrapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mItemTapListener != null) {
                    mItemTapListener.onTap(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public interface ItemTapListener {
        void onTap(Item item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView description;
        private TextView status;
        private TextView date;
        private CardView wrapLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            wrapLayout = (CardView) itemView.findViewById(R.id.card_view);
            description = (TextView) itemView.findViewById(R.id.description_value);
            status = (TextView) itemView.findViewById(R.id.status_value);
            date = (TextView) itemView.findViewById(R.id.date_value);
        }
    }
}
