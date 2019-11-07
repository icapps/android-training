package com.worldline.nicolaldi;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.worldline.nicolaldi.adapter.ShoppingCartAdapter;
import com.worldline.nicolaldi.adapter.StoreItemAdapter;
import com.worldline.nicolaldi.model.CartItem;
import com.worldline.nicolaldi.model.StoreItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements StoreItemAdapter.OnAdapterPositionClickListener {

    private List<StoreItem> storeItems;
    private ShoppingCartAdapter shoppingCartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupGrid();
        setupShoppingCart();
    }

    @Override
    public void onPositionClicked(int position) {
        Log.d("MainActivity", "Clicked on position -> " + position + " this is product: " + storeItems.get(position));
        shoppingCartAdapter.addNewItem(new CartItem(storeItems.get(position), new Random().nextInt(5)));

        ((RecyclerView)findViewById(R.id.shoppingcart_view)).scrollToPosition(0);
    }

    private void setupGrid() {
        List<StoreItem> items = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            items.add(new StoreItem(getString(R.string.item_apple), 10.0, "/kg", R.drawable.apple));
        }

        StoreItemAdapter adapter = new StoreItemAdapter(items, this);

        RecyclerView recyclerView = findViewById(R.id.items_container);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        storeItems = items;
    }

    private void setupShoppingCart() {
        ShoppingCartAdapter adapter = new ShoppingCartAdapter();

        RecyclerView recyclerView = findViewById(R.id.shoppingcart_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        shoppingCartAdapter = adapter;
    }

}
