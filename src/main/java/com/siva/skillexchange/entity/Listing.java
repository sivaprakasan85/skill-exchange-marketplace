package com.siva.skillexchange.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "listings")
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String category;

    private String priceOrFree;

    // ---------- Constructors ----------

    public Listing() {
    }

    public Listing(User user, String title, String description, String category, String priceOrFree) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priceOrFree = priceOrFree;
    }

    // ---------- Getters and Setters ----------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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