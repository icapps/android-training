package com.worldline.nicolaldi;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.worldline.nicolaldi.adapter.StoreItemAdapter;
import com.worldline.nicolaldi.model.StoreItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements StoreItemAdapter.OnAdapterPositionClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout parent = findViewById(R.id.parent);

        for (int i = 0; i <= 10; ++i) {
            View view = getLayoutInflater().inflate(R.layout.layout_cart_entry, parent, false);
            parent.addView(view);
        }

        List<StoreItem> items = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            items.add(new StoreItem(getString(R.string.item_apple), 10.0, "/kg", R.drawable.apple));
        }

        StoreItemAdapter adapter = new StoreItemAdapter(items, this);

        RecyclerView recyclerView = findViewById(R.id.items_container);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPositionClicked(int position) {
        Log.d("MainActivity", "Clicked on position -> " + position);
    }

}
