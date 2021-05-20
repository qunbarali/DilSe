package com.example.dilse.models;

public class ProductDetails {

    String title, subtitle, brand, imgUrl;
    boolean inStock;
    int price;

    public ProductDetails(String title, String subtitle, String brand, String imgUrl, boolean inStock, int price) {
        this.title = title;
        this.subtitle = subtitle;
        this.brand = brand;
        this.imgUrl = imgUrl;
        this.inStock = inStock;
        this.price = price;
    }

    public ProductDetails(){
        //Required Empty Constructor
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
