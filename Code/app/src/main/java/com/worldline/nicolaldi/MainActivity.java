package com.worldline.nicolaldi;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.worldline.nicolaldi.adapter.ProductPagerAdapter;
import com.worldline.nicolaldi.adapter.ShoppingCartAdapter;
import com.worldline.nicolaldi.fragment.ProductsFragment;
import com.worldline.nicolaldi.model.CartItem;
import com.worldline.nicolaldi.model.StoreItem;
import com.worldline.nicolaldi.model.TransactionModel;
import com.worldline.nicolaldi.receiver.NetworkChangeReceiver;
import com.worldline.nicolaldi.service.BoundTransactionSaverService;
import com.worldline.nicolaldi.service.NicolaldiService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends DaggerAppCompatActivity implements ProductsFragment.StoreItemClickListener {

    private static final int REQUEST_PAY = 10;
    private static final int REQUEST_LOCATION_PERMISSION = 10;

    private ShoppingCartAdapter shoppingCartAdapter;
    private NetworkChangeReceiver networkChangeReceiver;
    private ServiceConnection serviceConnection;
    private BoundTransactionSaverService.TransactionSaverBinder serviceBinder;
    private List<Pair<Double, Location>> transactionSaveQueue = new LinkedList<>();

    @Inject
    NicolaldiService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (getSupportFragmentManager().findFragmentById(R.id.product_fragment_container) == null) {
            ProductsFragment fragment = new ProductsFragment();
            Bundle arguments = new Bundle();
            arguments.putString(ProductsFragment.ARGUMENT_NAMEKEY, "banana");
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.product_fragment_container, fragment, "products!")
                    .commit();
        }

        TabLayout layout = findViewById(R.id.products_tabs);

        List<String> tabs = new ArrayList<>();
        tabs.add("banana");
        tabs.add("lemon");
        tabs.add("strawberry");

        ViewPager pager = findViewById(R.id.product_fragment_container);
        pager.setAdapter(new ProductPagerAdapter(getSupportFragmentManager(), tabs));

        layout.setupWithViewPager(pager);

        setupShoppingCart();
        setupShareButton();
        setupPayButton();
    }

    @Override
    protected void onStart() {
        super.onStart();
        networkChangeReceiver = NetworkChangeReceiver.startListeningForNetworkChanges(this);

        Intent intent = new Intent(this, BoundTransactionSaverService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                serviceBinder = ((BoundTransactionSaverService.TransactionSaverBinder) service);
                for (Pair<Double, Location> doubleLocationPair : transactionSaveQueue) {
                    serviceBinder.saveTransaction(doubleLocationPair.first, doubleLocationPair.second);
                }
                transactionSaveQueue.clear();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                serviceBinder = null;
            }
        };

        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        NetworkChangeReceiver.stopListeningForNetworkChanges(this, networkChangeReceiver);
        networkChangeReceiver = null;
        unbindService(serviceConnection);
        serviceBinder = null;
    }

    @Override
    public void onStoreItemClicked(StoreItem item) {
        shoppingCartAdapter.addNewItem(new CartItem(item, new Random().nextInt(5)));

        ((RecyclerView) findViewById(R.id.shoppingcart_view)).scrollToPosition(0);
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

    private void saveTransaction() {
        double totalAmount = shoppingCartAdapter.getTotalAmount();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = getLastKnownLocation(locationManager);

//        Intent intent = TransactionSaverServiceNormal.createIntent(this, totalAmount, location);
//        startService(intent);

        if (serviceBinder != null)
            serviceBinder.saveTransaction(totalAmount, location);
        else
            transactionSaveQueue.add(new Pair<>(totalAmount, location));
    }

    private void sendTransaction() {
        String id = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis();
        double totalAmount = shoppingCartAdapter.getTotalAmount();

        Call<TransactionModel> transactionModelCall = service
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

}
