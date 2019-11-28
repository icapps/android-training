package com.worldline.nicolaldi.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.worldline.nicolaldi.model.StoreItem;
import com.worldline.nicolaldi.util.DatabaseLoader;

import java.util.List;

/**
 * @author Nicola Verbeeck
 */
public class ProductsFragmentViewModel extends AndroidViewModel implements DatabaseLoader.DatabaseLoadListener {

    private MutableLiveData<List<StoreItem>> storeItems = new MutableLiveData<>();

    public ProductsFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<StoreItem>> getStoreItems() {
        return storeItems;
    }

    public void setupWithKey(String nameKey) {
        DatabaseLoader.loadDatabase(getApplication(), nameKey, this);
    }

    @Override
    public void onDatabaseLoaded(List<StoreItem> items) {
        storeItems.setValue(items);
    }

    public void removeProduct(int atPosition) {
        final List<StoreItem> value = storeItems.getValue();
        value.remove(atPosition);
        storeItems.setValue(value);
    }

}
