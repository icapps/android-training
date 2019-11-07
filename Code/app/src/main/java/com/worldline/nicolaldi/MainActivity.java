package com.worldline.nicolaldi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
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

    private static final int REQUEST_PAY = 10;

    private List<StoreItem> storeItems;
    private ShoppingCartAdapter shoppingCartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupGrid();
        setupShoppingCart();
        setupShareButton();
        setupPayButton();
    }

    @Override
    public void onPositionClicked(int position) {
        Log.d("MainActivity", "Clicked on position -> " + position + " this is product: " + storeItems.get(position));
        shoppingCartAdapter.addNewItem(new CartItem(storeItems.get(position), new Random().nextInt(5)));

        ((RecyclerView) findViewById(R.id.shoppingcart_view)).scrollToPosition(0);
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

    private void setupShareButton() {
        findViewById(R.id.button_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Hi, I am using Nicolaldi, I am now broke!");

                startActivity(intent);
            }
        });
    }

    private void setupPayButton() {
        final Activity thisRef = this;
        findViewById(R.id.button_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(thisRef, DeeplinkActivity.class);
                startActivityForResult(intent, REQUEST_PAY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_PAY) {
            if (resultCode == Activity.RESULT_OK) {
                //OK!
                shoppingCartAdapter.clearCart();
            } else if (resultCode == Activity.RESULT_FIRST_USER) {
                String reason = data.getStringExtra("reason");
                Log.d("MainActivity", "Result from pay: " + reason);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
