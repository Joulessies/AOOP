package com.cofitearia.milktea.models;

import java.math.BigDecimal;

/**
 * Sale item model representing individual items in a sale
 */
public class SaleItem {
    private int id;
    private Sale sale;
    private Product product;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String notes;
    
    public SaleItem() {
        this.quantity = 1;
        this.unitPrice = BigDecimal.ZERO;
        this.totalPrice = BigDecimal.ZERO;
    }
    
    public SaleItem(Product product, int quantity, BigDecimal unitPrice) {
        this();
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        calculateTotalPrice();
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Sale getSale() {
        return sale;
    }
    
    public void setSale(Sale sale) {
        this.sale = sale;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateTotalPrice();
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        calculateTotalPrice();
    }
    
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    /**
     * Calculate total price based on quantity and unit price
     */
    private void calculateTotalPrice() {
        if (unitPrice != null && quantity > 0) {
            totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        } else {
            totalPrice = BigDecimal.ZERO;
        }
    }
    
    /**
     * Get formatted description for accessibility
     */
    public String getFormattedDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(product.getName());
        sb.append(", Quantity: ").append(quantity);
        sb.append(", Unit Price: ₱").append(unitPrice);
        sb.append(", Total: ₱").append(totalPrice);
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return product.getName() + " x" + quantity + " = ₱" + totalPrice;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SaleItem saleItem = (SaleItem) obj;
        return id == saleItem.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
