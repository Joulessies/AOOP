package com.cofitearia.milktea.models;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Product model representing items in the inventory
 * Includes accessibility considerations for product descriptions
 */
public class Product {
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private String barcode;
    private String unit; // e.g., "piece", "kg", "liter"
    private LocalDate dateCreated;
    private LocalDate dateModified;
    private boolean isActive;
    
    // Accessibility-related fields
    private String altText; // For screen readers
    private String largeTextDescription; // For visually impaired users
    
    public Product() {
        this.dateCreated = LocalDate.now();
        this.dateModified = LocalDate.now();
        this.isActive = true;
    }
    
    public Product(String name, String description, BigDecimal price, String category) {
        this();
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
        this.dateModified = LocalDate.now();
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
        this.dateModified = LocalDate.now();
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
        this.dateModified = LocalDate.now();
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
        this.dateModified = LocalDate.now();
    }
    
    public String getBarcode() {
        return barcode;
    }
    
    public void setBarcode(String barcode) {
        this.barcode = barcode;
        this.dateModified = LocalDate.now();
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
        this.dateModified = LocalDate.now();
    }
    
    public LocalDate getDateCreated() {
        return dateCreated;
    }
    
    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    public LocalDate getDateModified() {
        return dateModified;
    }
    
    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
        this.dateModified = LocalDate.now();
    }
    
    public String getAltText() {
        return altText;
    }
    
    public void setAltText(String altText) {
        this.altText = altText;
    }
    
    public String getLargeTextDescription() {
        return largeTextDescription;
    }
    
    public void setLargeTextDescription(String largeTextDescription) {
        this.largeTextDescription = largeTextDescription;
    }
    
    /**
     * Get formatted price string for accessibility
     */
    public String getFormattedPrice() {
        if (price != null) {
            return String.format("₱%.2f", price);
        }
        return "Price not set";
    }
    
    /**
     * Get full description for screen readers
     */
    public String getFullDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        if (description != null && !description.isEmpty()) {
            sb.append(", ").append(description);
        }
        sb.append(", Price: ").append(getFormattedPrice());
        if (category != null && !category.isEmpty()) {
            sb.append(", Category: ").append(category);
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return name + " - " + getFormattedPrice();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return id == product.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
