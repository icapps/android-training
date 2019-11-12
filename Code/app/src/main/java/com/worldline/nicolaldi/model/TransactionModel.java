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

    public String getTransactionId() {
        return transactionId;
    }

    public double getTotal() {
        return total;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "TransactionModel{" +
                "transactionId='" + transactionId + '\'' +
                ", total=" + total +
                ", timestamp=" + timestamp +
                '}';
    }
}
