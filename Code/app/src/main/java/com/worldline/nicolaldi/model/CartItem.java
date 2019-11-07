package com.worldline.nicolaldi.model;

/**
 * @author Nicola Verbeeck
 */
public class CartItem {

    private final StoreItem product;
    private final int numberOfTimes;

    public CartItem(StoreItem product, int numberOfTimes) {
        this.product = product;
        this.numberOfTimes = numberOfTimes;
    }

    public StoreItem getProduct() {
        return product;
    }

    public int getNumberOfTimes() {
        return numberOfTimes;
    }

    public double getTotalCost() {
        return product.getPrice() * numberOfTimes;
    }
}
