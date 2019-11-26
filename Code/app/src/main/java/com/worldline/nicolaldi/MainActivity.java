package com.worldline.nicolaldi;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.worldline.nicolaldi.adapter.ShoppingCartAdapter;
import com.worldline.nicolaldi.adapter.StoreItemAdapter;
import com.worldline.nicolaldi.model.CartItem;
import com.worldline.nicolaldi.model.StoreItem;
import com.worldline.nicolaldi.model.TransactionModel;
import com.worldline.nicolaldi.receiver.NetworkChangeReceiver;
import com.worldline.nicolaldi.service.TransactionSaverService;
import com.worldline.nicolaldi.util.DatabaseCreator;
import com.worldline.nicolaldi.util.DatabaseLoader;
import com.worldline.nicolaldi.util.TransactionSaver;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements StoreItemAdapter.OnAdapterPositionClickListener,
        DatabaseLoader.DatabaseLoadListener {

    private static final int REQUEST_PAY = 10;
    private static final int REQUEST_LOCATION_PERMISSION = 10;

    private List<StoreItem> storeItems;
    private ShoppingCartAdapter shoppingCartAdapter;
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadStoreFromDatabase();
        //loadStore2();
        setupShoppingCart();
        setupShareButton();
        setupPayButton();
    }

    @Override
    protected void onStart() {
        super.onStart();
        networkChangeReceiver = NetworkChangeReceiver.startListeningForNetworkChanges(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        NetworkChangeReceiver.stopListeningForNetworkChanges(this, networkChangeReceiver);
        networkChangeReceiver = null;
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
                saveTransaction();
                sendTransaction();
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
        DatabaseLoader.loadDatabase(this, this);
    }

    private void saveTransaction() {
        double totalAmount = shoppingCartAdapter.getTotalAmount();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = getLastKnownLocation(locationManager);

        Intent intent = TransactionSaverService.createIntent(this, totalAmount, location);
        startService(intent);
    }

    private void sendTransaction() {
        String id = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis();
        double totalAmount = shoppingCartAdapter.getTotalAmount();

        Call<TransactionModel> transactionModelCall = ((MyApplication) getApplication()).service
                .sendTransaction(new TransactionModel(id, totalAmount, timestamp));


        transactionModelCall.enqueue(new Callback<TransactionModel>() {
            @Override
            public void onResponse(Call<TransactionModel> call, Response<TransactionModel> response) {
                TransactionModel model = response.body();
                Log.d("MainActivity", "Got response from server: " + model);
            }

            @Override
            public void onFailure(Call<TransactionModel> call, Throwable t) {
                Log.e("MainActivity", "Failed to call:", t);
            }
        });
    }

    private void createDB() {
        DatabaseCreator.createDatabase(this);
    }

    private Location getLastKnownLocation(LocationManager locationManager) {
        final int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (permissions.length == 0) {
                Toast.makeText(this, "You permanently denied our permission", Toast.LENGTH_LONG).show();
                return;
            }
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You granted our permission", Toast.LENGTH_LONG).show();
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(this, "You permanently denied our permission", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "You denied our permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onDatabaseLoaded(List<StoreItem> items) {
        setupGrid(items);
    }
}
