package com.worldline.nicolaldi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.worldline.nicolaldi.R;
import com.worldline.nicolaldi.model.StoreItem;

/**
 * @author Nicola Verbeeck
 */
public class ProductCardView extends RelativeLayout {

    private TextView titleView;
    private ImageView imageView;
    private TextView unitView;
    private TextView priceView;

    public ProductCardView(Context context) {
        super(context);
    }

    public ProductCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProductCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        titleView = findViewById(R.id.card_title);
        imageView = findViewById(R.id.card_image);
        unitView = findViewById(R.id.card_unit);
        priceView = findViewById(R.id.card_price);
    }

    public void bind(StoreItem item) {
        titleView.setText(item.getName());
        unitView.setText(item.getUnit());
        priceView.setText("" + item.getPrice());
        imageView.setImageResource(item.getImageResource());
    }

}
