package com.example.tokopari.model;

public class WishlistItem {
    private String name;
    private String imageUrl;
    private double price;

    public WishlistItem(String name, String imageUrl, double price) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getPrice() {
        return price;
    }
}
