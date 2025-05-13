package com.example.travelapp.Domain;

import java.io.Serializable;

public class WishlistItem implements Serializable {
    private int id;
    private String title;
    private String address;
    private String imageUrl;
    private int price;
    private double score;

    public WishlistItem() {}

    public WishlistItem(int id, String title, String address, String imageUrl, int price, double score) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.imageUrl = imageUrl;
        this.price = price;
        this.score = score;
    }

    // Getter và Setter
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAddress() { return address; }
    public String getPic() { return imageUrl; }
    public int getPrice() { return price; }
    public double getScore() { return score; }

}
