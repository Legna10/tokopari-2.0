package com.example.tokopari.model;

public class Product {
    private String name;
    private int price;
    private String imageUrl;
    private boolean flashSale; // Menambahkan properti ini

    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(Product.class)
    }

    public Product(String name, int price, String imageUrl, boolean flashSale) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.flashSale = flashSale;
    }

    // Getter dan setter untuk setiap properti

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isFlashSale() {
        return flashSale;
    }

    public void setFlashSale(boolean flashSale) {
        this.flashSale = flashSale;
    }
}
