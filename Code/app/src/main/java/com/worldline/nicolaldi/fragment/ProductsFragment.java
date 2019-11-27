package com.worldline.nicolaldi.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.worldline.nicolaldi.R;
import com.worldline.nicolaldi.adapter.StoreItemAdapter;
import com.worldline.nicolaldi.model.StoreItem;
import com.worldline.nicolaldi.util.DatabaseLoader;

import java.sql.DatabaseMetaData;
import java.util.List;

/**
 * @author Nicola Verbeeck
 */
public class ProductsFragment extends BaseFragment implements StoreItemAdapter.OnAdapterPositionClickListener,
        DatabaseLoader.DatabaseLoadListener {

    private RecyclerView itemsRecycler;
    private List<StoreItem> storeItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        itemsRecycler = view.findViewById(R.id.items_recycler);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        DatabaseLoader.loadDatabase(requireContext(), this);
    }

    private void setupGrid(List<StoreItem> items) {
        Log.d("TRACE", "Starting");

        StoreItemAdapter adapter = new StoreItemAdapter(items, this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 5);

        itemsRecycler.setLayoutManager(gridLayoutManager);
        itemsRecycler.setAdapter(adapter);

        storeItems = items;

        Log.d("TRACE", "Stopping");
    }

    @Override
    public void onDatabaseLoaded(List<StoreItem> items) {
        setupGrid(items);
    }

    @Override
    public void onPositionClicked(int position) {
        StoreItem item = storeItems.get(position);

        findParent(StoreItemClickListener.class).onStoreItemClicked(item);
    }

    public interface StoreItemClickListener {
        void onStoreItemClicked(StoreItem item);
    }
}
