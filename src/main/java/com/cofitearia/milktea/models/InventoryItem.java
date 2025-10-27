package com.cofitearia.milktea.models;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Inventory item model representing stock levels and tracking
 * Includes expiration date tracking and low stock alerts
 */
public class InventoryItem {
    private int id;
    private Product product;
    private int currentStock;
    private int minimumStock;
    private int maximumStock;
    private BigDecimal costPrice;
    private LocalDate expirationDate;
    private String supplier;
    private String location; // Shelf location for accessibility
    private LocalDate lastRestocked;
    private LocalDate dateCreated;
    private LocalDate dateModified;
    private boolean isActive;
    
    // Alert thresholds
    private int lowStockThreshold;
    private int criticalStockThreshold;
    
    public InventoryItem() {
        this.dateCreated = LocalDate.now();
        this.dateModified = LocalDate.now();
        this.isActive = true;
        this.lowStockThreshold = 10;
        this.criticalStockThreshold = 5;
    }
    
    public InventoryItem(Product product, int currentStock, int minimumStock) {
        this();
        this.product = product;
        this.currentStock = currentStock;
        this.minimumStock = minimumStock;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
        this.dateModified = LocalDate.now();
    }
    
    public int getCurrentStock() {
        return currentStock;
    }
    
    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
        this.dateModified = LocalDate.now();
    }
    
    public int getMinimumStock() {
        return minimumStock;
    }
    
    public void setMinimumStock(int minimumStock) {
        this.minimumStock = minimumStock;
        this.dateModified = LocalDate.now();
    }
    
    public int getMaximumStock() {
        return maximumStock;
    }
    
    public void setMaximumStock(int maximumStock) {
        this.maximumStock = maximumStock;
        this.dateModified = LocalDate.now();
    }
    
    public BigDecimal getCostPrice() {
        return costPrice;
    }
    
    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
        this.dateModified = LocalDate.now();
    }
    
    public LocalDate getExpirationDate() {
        return expirationDate;
    }
    
    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
        this.dateModified = LocalDate.now();
    }
    
    public String getSupplier() {
        return supplier;
    }
    
    public void setSupplier(String supplier) {
        this.supplier = supplier;
        this.dateModified = LocalDate.now();
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
        this.dateModified = LocalDate.now();
    }
    
    public LocalDate getLastRestocked() {
        return lastRestocked;
    }
    
    public void setLastRestocked(LocalDate lastRestocked) {
        this.lastRestocked = lastRestocked;
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
    
    public int getLowStockThreshold() {
        return lowStockThreshold;
    }
    
    public void setLowStockThreshold(int lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
        this.dateModified = LocalDate.now();
    }
    
    public int getCriticalStockThreshold() {
        return criticalStockThreshold;
    }
    
    public void setCriticalStockThreshold(int criticalStockThreshold) {
        this.criticalStockThreshold = criticalStockThreshold;
        this.dateModified = LocalDate.now();
    }
    
    // Business Logic Methods
    
    /**
     * Check if stock is low
     */
    public boolean isLowStock() {
        return currentStock <= lowStockThreshold;
    }
    
    /**
     * Check if stock is critical
     */
    public boolean isCriticalStock() {
        return currentStock <= criticalStockThreshold;
    }
    
    /**
     * Check if item is expired
     */
    public boolean isExpired() {
        return expirationDate != null && expirationDate.isBefore(LocalDate.now());
    }
    
    /**
     * Check if item is expiring soon (within 7 days)
     */
    public boolean isExpiringSoon() {
        return expirationDate != null && 
               expirationDate.isBefore(LocalDate.now().plusDays(7)) &&
               expirationDate.isAfter(LocalDate.now());
    }
    
    /**
     * Get stock status for accessibility
     */
    public String getStockStatus() {
        if (isCriticalStock()) {
            return "Critical stock level";
        } else if (isLowStock()) {
            return "Low stock level";
        } else {
            return "Adequate stock level";
        }
    }
    
    /**
     * Get formatted stock information for screen readers
     */
    public String getFormattedStockInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(product.getName());
        sb.append(", Current stock: ").append(currentStock);
        sb.append(", Minimum required: ").append(minimumStock);
        sb.append(", Status: ").append(getStockStatus());
        
        if (expirationDate != null) {
            sb.append(", Expires: ").append(expirationDate.toString());
            if (isExpired()) {
                sb.append(" (Expired)");
            } else if (isExpiringSoon()) {
                sb.append(" (Expiring soon)");
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Calculate reorder quantity
     */
    public int getReorderQuantity() {
        return maximumStock - currentStock;
    }
    
    /**
     * Add stock to current inventory
     */
    public void addStock(int quantity) {
        this.currentStock += quantity;
        this.lastRestocked = LocalDate.now();
        this.dateModified = LocalDate.now();
    }
    
    /**
     * Remove stock from current inventory
     */
    public boolean removeStock(int quantity) {
        if (currentStock >= quantity) {
            this.currentStock -= quantity;
            this.dateModified = LocalDate.now();
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return product.getName() + " - Stock: " + currentStock;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        InventoryItem that = (InventoryItem) obj;
        return id == that.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
