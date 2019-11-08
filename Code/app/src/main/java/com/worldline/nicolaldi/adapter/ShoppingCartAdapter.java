package com.worldline.nicolaldi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.worldline.nicolaldi.R;
import com.worldline.nicolaldi.model.CartItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nicola Verbeeck
 */
public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartViewHolder> {

    private List<CartItem> shoppingCart = new ArrayList<>();

    public void addNewItem(CartItem item) {
        shoppingCart.add(0, item);
        notifyItemInserted(0);
    }

    public void clearCart() {
        int oldSize = shoppingCart.size();
        shoppingCart.clear();
        notifyItemRangeRemoved(0, oldSize);
    }

    @NonNull
    @Override
    public ShoppingCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.layout_cart_entry, parent, false);

        return new ShoppingCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingCartViewHolder holder, int position) {
        holder.bind(shoppingCart.get(position));
    }

    @Override
    public int getItemCount() {
        return shoppingCart.size();
    }

    public double getTotalAmount() {
        double total = 0.0;
        for (CartItem cartItem : shoppingCart) {
            total += cartItem.getTotalCost();
        }
        return total;
    }

    public static class ShoppingCartViewHolder extends RecyclerView.ViewHolder {

        private final ImageView cartImage;
        private final TextView cartTitle;
        private final TextView cartUnitPrice;
        private final TextView cartTotalPrice;

        public ShoppingCartViewHolder(@NonNull View itemView) {
            super(itemView);

            cartImage = itemView.findViewById(R.id.cart_product_image);
            cartTitle = itemView.findViewById(R.id.cart_product_title);
            cartUnitPrice = itemView.findViewById(R.id.cart_product_unit_price);
            cartTotalPrice = itemView.findViewById(R.id.cart_total_price);
        }

        public void bind(CartItem item) {
            cartImage.setImageResource(item.getProduct().getImageResource());
            cartTitle.setText(item.getProduct().getName());
            cartUnitPrice.setText(String.format("%d x %f", item.getNumberOfTimes(), item.getProduct().getPrice()));
            cartTotalPrice.setText("" + item.getTotalCost());
        }

    }

}
