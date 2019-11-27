package com.worldline.nicolaldi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.worldline.nicolaldi.R;

/**
 * @author Nicola Verbeeck
 */
public class ProductCardView2 extends RelativeLayout {
    public ProductCardView2(@NonNull Context context) {
        this(context, null);
    }

    public ProductCardView2(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProductCardView2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.layout_card, this, true);
    }
}
