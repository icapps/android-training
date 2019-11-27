package com.worldline.nicolaldi.fragment;

import android.app.Activity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author Nicola Verbeeck
 */
public abstract class BaseFragment extends Fragment {

    @SuppressWarnings("unchecked")
    @Nullable
    protected <T> T findParent(Class<T> toFind) {
        Fragment toCheck = getParentFragment();

        while (toCheck != null) {
            if (toFind.isAssignableFrom(toCheck.getClass()))
                return (T) toCheck;
            toCheck = toCheck.getParentFragment();
        }

        Activity activity = getActivity();
        if (activity == null)
            return null;

        if (toFind.isAssignableFrom(activity.getClass()))
            return (T) activity;

        return null;
    }

}
