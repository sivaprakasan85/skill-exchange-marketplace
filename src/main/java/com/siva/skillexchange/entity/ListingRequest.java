package com.siva.skillexchange.entity;

public class ListingRequest {

    private Integer userId;
    private String title;
    private String description;
    private String category;
    private String priceOrFree;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriceOrFree() {
        return priceOrFree;
    }

    public void setPriceOrFree(String priceOrFree) {
        this.priceOrFree = priceOrFree;
    }
}