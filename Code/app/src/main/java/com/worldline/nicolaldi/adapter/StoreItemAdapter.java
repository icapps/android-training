package com.worldline.nicolaldi.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.worldline.nicolaldi.R;
import com.worldline.nicolaldi.model.StoreItem;

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

    @NonNull
    @Override
    public StoreItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.layout_card, parent, false);

        ++count;

        Log.d("Recycler", "Created total of: " + count + " views");

        return new StoreItemViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreItemViewHolder holder, int position) {
        StoreItem item = storeItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return storeItems.size();
    }

    public static class StoreItemViewHolder extends RecyclerView.ViewHolder {

        private final ImageView cardImage;
        private final TextView cardTitle;
        private final TextView cardPrice;
        private final TextView cardUnit;

        public StoreItemViewHolder(@NonNull View itemView, final OnAdapterPositionClickListener clickListener) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    clickListener.onPositionClicked(position);
                }
            });

            cardTitle = itemView.findViewById(R.id.card_title);
            cardPrice = itemView.findViewById(R.id.card_price);
            cardUnit = itemView.findViewById(R.id.card_unit);
            cardImage = itemView.findViewById(R.id.card_image);
        }

        public void bind(StoreItem item) {
            cardImage.setImageResource(item.getImageResource());
            cardTitle.setText(item.getName());
            cardUnit.setText(item.getUnit());
            cardPrice.setText("" + item.getPrice());
        }

    }

    public interface OnAdapterPositionClickListener {

        void onPositionClicked(int position);

    }

}
