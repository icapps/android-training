package com.worldline.nicolaldi.model;

import com.worldline.nicolaldi.R;

/**
 * @author Nicola Verbeeck
 */
public class StoreItemJson {

    private final String name;
    private final String unit;
    private final double price;
    private final String imageType;

    public StoreItemJson(String name, String unit, double price, String imageType) {
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.imageType = imageType;
    }

    public StoreItem toStoreItem() {
        return new StoreItem(name, price, unit, determineImageResource());
    }

    private int determineImageResource() {
        switch (imageType) {
            case "apple":
                return R.drawable.apple;
            case "banana":
                return R.drawable.banana;
            case "berry":
                return R.drawable.berry;
            case "grape":
                return R.drawable.grape;
            case "grapefruit":
                return R.drawable.grapefruit;
            case "lemon":
                return R.drawable.lemon;
            case "orange":
                return R.drawable.orange;
            case "pear":
                return R.drawable.pear;
            case "strawberry":
                return R.drawable.strawberry;
            case "watermelon":
                return R.drawable.watermelon;
            default:
                return 0;
        }
    }

}
