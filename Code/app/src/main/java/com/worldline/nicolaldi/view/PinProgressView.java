package com.worldline.nicolaldi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.worldline.nicolaldi.R;

/**
 * @author Nicola Verbeeck
 */
public class PinProgressView extends LinearLayout {

    private final int numPinItems;

    public PinProgressView(Context context) {
        this(context, null, 0);
    }

    public PinProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PinProgressView, defStyleAttr, 0);

        numPinItems = array.getInteger(R.styleable.PinProgressView_numberOfPinItems, 0);
        int orbSize = array.getDimensionPixelSize(R.styleable.PinProgressView_orbSize, 0);
        int orbMargin = array.getDimensionPixelSize(R.styleable.PinProgressView_orbMargin, 0);

        array.recycle();

        for (int i = 0; i < numPinItems; ++i) {
            ImageView view = new ImageView(context);
            LayoutParams params = new LinearLayout.LayoutParams(orbSize, orbSize);
            view.setImageResource(R.drawable.orb_empty);

            if (i != 0) {
                params.leftMargin = orbMargin;
            }

            addView(view, params);
        }
    }

    public void setCurrentPinLength(int numberOfPinItems) {
        for (int i = 0; i < numPinItems; ++i) {
            ImageView child = (ImageView) getChildAt(i);

            child.setImageResource(numberOfPinItems > i ? R.drawable.orb_filled : R.drawable.orb_empty);
        }
    }

}
