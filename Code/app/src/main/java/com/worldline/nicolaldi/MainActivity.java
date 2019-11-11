package com.worldline.nicolaldi;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.worldline.nicolaldi.util.DatabaseCreator;
import com.worldline.nicolaldi.util.DatabaseLoader;

import java.lang.ref.WeakReference;
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

        loadStoreFromDatabase();
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

    private void loadStore() {
        final Handler handler = new Handler(Looper.getMainLooper());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final List<StoreItem> items = new ArrayList<>();
                for (int i = 0; i < 1000000; i++) {
                    items.add(new StoreItem(getString(R.string.item_apple), 10.0, "/kg", R.drawable.apple));
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        setupGrid(items);
                    }
                });
            }
        });
        thread.start();
    }

    private void loadStore2() {
        new StoreLoadingTask(this).execute();
    }

    private void setupGrid(List<StoreItem> items) {
        Log.d("TRACE", "Starting");

        StoreItemAdapter adapter = new StoreItemAdapter(items, this);

        RecyclerView recyclerView = findViewById(R.id.items_container);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        storeItems = items;

        Log.d("TRACE", "Stopping");
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
                intent.putExtra("total_amount", shoppingCartAdapter.getTotalAmount());
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
            } else if (resultCode == Activity.RESULT_FIRST_USER && data != null) {
                String reason = data.getStringExtra("reason");
                Log.d("MainActivity", "Result from pay: " + reason);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public class StoreLoadingTask extends AsyncTask<Void, Void, List<StoreItem>> {

        WeakReference<MainActivity> reference;

        public StoreLoadingTask(MainActivity activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        protected List<StoreItem> doInBackground(Void... voids) {
            final List<StoreItem> items = new ArrayList<>();
            for (int i = 0; i < 1000000; i++) {
                items.add(new StoreItem(getString(R.string.item_apple), 10.0, "/kg", R.drawable.apple));
            }
            return items;
        }

        @Override
        protected void onPostExecute(List<StoreItem> storeItems) {
            super.onPostExecute(storeItems);

            MainActivity activity = reference.get();
            if (activity != null)
                activity.setupGrid(storeItems);
        }
    }

    private void loadStoreFromDatabase() {
        DatabaseLoader.loadDatabase(this, new DatabaseLoader.DatabaseLoadListener() {
            @Override
            public void onDatabaseLoaded(List<StoreItem> items) {
                setupGrid(items);
            }
        });
    }

    private void createDB() {
        DatabaseCreator.createDatabase(this);
    }

}
