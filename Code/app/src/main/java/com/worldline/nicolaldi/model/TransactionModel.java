package com.worldline.nicolaldi.model;

/**
 * @author Nicola Verbeeck
 * <p>
 * Posting to https://jsonplaceholder.typicode.com/posts
 */
public class TransactionModel {

    private final String transactionId;
    private final double total;
    private final long timestamp;

    public TransactionModel(String transactionId, double total, long timestamp) {
        this.transactionId = transactionId;
        this.total = total;
        this.timestamp = timestamp;
    }

}
