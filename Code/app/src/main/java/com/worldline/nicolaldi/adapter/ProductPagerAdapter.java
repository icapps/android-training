package com.worldline.nicolaldi.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.worldline.nicolaldi.fragment.ProductsFragment;

import java.util.List;

/**
 * @author Nicola Verbeeck
 */
public class ProductPagerAdapter extends FragmentStatePagerAdapter {

    private final List<String> tabTexts;

    public ProductPagerAdapter(@NonNull FragmentManager fm, List<String> tabTexts) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.tabTexts = tabTexts;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ProductsFragment.ARGUMENT_NAMEKEY, tabTexts.get(position));
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public int getCount() {
        return tabTexts.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTexts.get(position);
    }
}
