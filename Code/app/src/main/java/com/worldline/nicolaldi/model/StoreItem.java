package com.worldline.nicolaldi.model;

/**
 * @author Nicola Verbeeck
 */
public class StoreItem {

    private final String name;
    private final double price;
    private final String unit;
    private final int imageResource;

    public StoreItem(String name, double price, String unit, int imageResource) {
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getUnit() {
        return unit;
    }

    public int getImageResource() {
        return imageResource;
    }
}
