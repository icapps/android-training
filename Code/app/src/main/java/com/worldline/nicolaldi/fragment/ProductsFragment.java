package com.worldline.nicolaldi.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.worldline.nicolaldi.R;
import com.worldline.nicolaldi.adapter.StoreItemAdapter;
import com.worldline.nicolaldi.model.StoreItem;
import com.worldline.nicolaldi.util.DatabaseLoader;
import com.worldline.nicolaldi.viewmodel.ProductsFragmentViewModel;

import java.util.Collections;
import java.util.List;

/**
 * @author Nicola Verbeeck
 */
public class ProductsFragment extends BaseFragment implements StoreItemAdapter.OnAdapterPositionClickListener {

    public static final String ARGUMENT_NAMEKEY = "nameKey";

    private RecyclerView itemsRecycler;
    private List<StoreItem> storeItems;

    private ProductsFragmentViewModel viewModel;

    private StoreItemAdapter adapter = new StoreItemAdapter(Collections.<StoreItem>emptyList(), this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                .get(ProductsFragmentViewModel.class);

        viewModel.setupWithKey(getArguments().getString(ARGUMENT_NAMEKEY));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        itemsRecycler = view.findViewById(R.id.items_recycler);

        viewModel.getStoreItems().observe(getViewLifecycleOwner(), new Observer<List<StoreItem>>() {
            @Override
            public void onChanged(List<StoreItem> storeItems) {
                ProductsFragment.this.storeItems = storeItems;
                adapter.setStoreItems(storeItems);
            }
        });

        setupGrid();

        return view;
    }

    private void setupGrid() {
        if (itemsRecycler == null)
            return;

        Log.d("TRACE", "Starting");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 5);

        itemsRecycler.setLayoutManager(gridLayoutManager);
        itemsRecycler.setAdapter(adapter);

        Log.d("TRACE", "Stopping");
    }

    @Override
    public void onPositionClicked(int position) {
        StoreItem item = storeItems.get(position);

        findParent(StoreItemClickListener.class).onStoreItemClicked(item);
        viewModel.removeProduct(position);
    }

    public interface StoreItemClickListener {
        void onStoreItemClicked(StoreItem item);
    }
}
