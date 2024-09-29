package com.example.tokopari; // Sesuaikan dengan nama package yang digunakan

public class WishlistItem {
    private String title;
    private String imageUrl;
    private double price;

    // Constructor
    public WishlistItem(String title, String imageUrl, double price) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    // Getter dan Setter
    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getPrice() {
        return price;
    }
}
