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
 * Additional example of creating an adapter
 *
 * @author Nicola Verbeeck
 */
public class ShoppingCartAdapter2 extends RecyclerView.Adapter<ShoppingCartAdapter2.CartItemViewHolder> {

    private List<CartItem> cartItems = new ArrayList<>();

    public void addCartItem(CartItem item) {
        cartItems.add(0, item);
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_cart_entry, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        holder.bind(cartItems.get(position));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView cartProductImage;
        private TextView cartProductPrice;
        private TextView cartTotal;
        private TextView cartProductTitle;

        public CartItemViewHolder(View view) {
            super(view);

            cartProductImage = view.findViewById(R.id.cart_product_image);
            cartProductPrice = view.findViewById(R.id.cart_product_unit_price);
            cartTotal = view.findViewById(R.id.cart_total_price);
            cartProductTitle = view.findViewById(R.id.cart_product_title);
        }

        public void bind(CartItem cartItem) {
            cartProductImage.setImageResource(cartItem.getProduct().getImageResource());
            cartProductTitle.setText(cartItem.getProduct().getName());
            cartTotal.setText("" + cartItem.getTotalCost());
            cartProductPrice.setText(cartItem.getProduct().getPrice() + " " + cartItem.getProduct().getUnit());
        }
    }

}
