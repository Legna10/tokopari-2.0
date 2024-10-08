package com.example.tokopari.model;

public class TransactionItem {
    private String productName;
    private String status;
    private String date;

    public TransactionItem(String productName, String status, String date) {
        this.productName = productName;
        this.status = status;
        this.date = date;
    }

    public String getProductName() {
        return productName;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }
}
