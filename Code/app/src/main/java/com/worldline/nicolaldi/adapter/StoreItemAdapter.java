package com.worldline.nicolaldi.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.worldline.nicolaldi.R;
import com.worldline.nicolaldi.model.StoreItem;
import com.worldline.nicolaldi.view.ProductCardView;

import java.util.List;

/**
 * @author Nicola Verbeeck
 */
public class StoreItemAdapter extends RecyclerView.Adapter<StoreItemAdapter.StoreItemViewHolder> {

    private List<StoreItem> storeItems;
    private OnAdapterPositionClickListener clickListener;
    private int count;

    public StoreItemAdapter(List<StoreItem> storeItems, OnAdapterPositionClickListener clickListener) {
        this.storeItems = storeItems;
        this.clickListener = clickListener;
    }

    public void setStoreItems(List<StoreItem> items) {
        storeItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StoreItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.layout_card, parent, false);

        ++count;

        Log.d("Recycler", "Created total of: " + count + " views");

        return new StoreItemViewHolder((ProductCardView) view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreItemViewHolder holder, int position) {
        StoreItem item = storeItems.get(position);
        holder.bind(item, position);
    }

    @Override
    public int getItemCount() {
        return storeItems.size();
    }

    public static class StoreItemViewHolder extends RecyclerView.ViewHolder {

        private final ProductCardView cardView;

        public StoreItemViewHolder(@NonNull ProductCardView itemView, final OnAdapterPositionClickListener clickListener) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    clickListener.onPositionClicked(position);
                }
            });

            this.cardView = itemView;
        }

        public void bind(StoreItem item, int position) {
            cardView.bind(item);
        }

    }

    public interface OnAdapterPositionClickListener {

        void onPositionClicked(int position);

    }

}
