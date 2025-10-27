package com.cofitearia.milktea.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Sale model representing a transaction
 * Includes accessibility features for transaction tracking
 */
public class Sale {
    private int id;
    private String transactionNumber;
    private List<SaleItem> items;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal discount;
    private BigDecimal total;
    private String paymentMethod;
    private String customerInfo; // For accessibility - can include special needs
    private User cashier;
    private LocalDateTime saleDate;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
    private String notes;
    private boolean isVoided;
    
    // Accessibility-related fields
    private boolean accessibilityAssistanceUsed;
    private String accessibilityNotes;
    
    public Sale() {
        this.items = new ArrayList<>();
        this.saleDate = LocalDateTime.now();
        this.dateCreated = LocalDateTime.now();
        this.dateModified = LocalDateTime.now();
        this.isVoided = false;
        this.accessibilityAssistanceUsed = false;
        this.tax = BigDecimal.ZERO;
        this.discount = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
        this.subtotal = BigDecimal.ZERO;
    }
    
    public Sale(User cashier) {
        this();
        this.cashier = cashier;
        this.transactionNumber = generateTransactionNumber();
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTransactionNumber() {
        return transactionNumber;
    }
    
    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
        this.dateModified = LocalDateTime.now();
    }
    
    public List<SaleItem> getItems() {
        return items;
    }
    
    public void setItems(List<SaleItem> items) {
        this.items = items;
        this.dateModified = LocalDateTime.now();
        calculateTotals();
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
        this.dateModified = LocalDateTime.now();
    }
    
    public BigDecimal getTax() {
        return tax;
    }
    
    public void setTax(BigDecimal tax) {
        this.tax = tax;
        this.dateModified = LocalDateTime.now();
        calculateTotals();
    }
    
    public BigDecimal getDiscount() {
        return discount;
    }
    
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
        this.dateModified = LocalDateTime.now();
        calculateTotals();
    }
    
    public BigDecimal getTotal() {
        return total;
    }
    
    public void setTotal(BigDecimal total) {
        this.total = total;
        this.dateModified = LocalDateTime.now();
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        this.dateModified = LocalDateTime.now();
    }
    
    public String getCustomerInfo() {
        return customerInfo;
    }
    
    public void setCustomerInfo(String customerInfo) {
        this.customerInfo = customerInfo;
        this.dateModified = LocalDateTime.now();
    }
    
    public User getCashier() {
        return cashier;
    }
    
    public void setCashier(User cashier) {
        this.cashier = cashier;
        this.dateModified = LocalDateTime.now();
    }
    
    public LocalDateTime getSaleDate() {
        return saleDate;
    }
    
    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
        this.dateModified = LocalDateTime.now();
    }
    
    public LocalDateTime getDateCreated() {
        return dateCreated;
    }
    
    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    public LocalDateTime getDateModified() {
        return dateModified;
    }
    
    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
        this.dateModified = LocalDateTime.now();
    }
    
    public boolean isVoided() {
        return isVoided;
    }
    
    public void setVoided(boolean voided) {
        isVoided = voided;
        this.dateModified = LocalDateTime.now();
    }
    
    public boolean isAccessibilityAssistanceUsed() {
        return accessibilityAssistanceUsed;
    }
    
    public void setAccessibilityAssistanceUsed(boolean accessibilityAssistanceUsed) {
        this.accessibilityAssistanceUsed = accessibilityAssistanceUsed;
        this.dateModified = LocalDateTime.now();
    }
    
    public String getAccessibilityNotes() {
        return accessibilityNotes;
    }
    
    public void setAccessibilityNotes(String accessibilityNotes) {
        this.accessibilityNotes = accessibilityNotes;
        this.dateModified = LocalDateTime.now();
    }
    
    // Business Logic Methods
    
    /**
     * Add item to sale
     */
    public void addItem(SaleItem item) {
        items.add(item);
        calculateTotals();
        this.dateModified = LocalDateTime.now();
    }
    
    /**
     * Remove item from sale
     */
    public void removeItem(SaleItem item) {
        items.remove(item);
        calculateTotals();
        this.dateModified = LocalDateTime.now();
    }
    
    /**
     * Calculate totals based on items and adjustments
     */
    public void calculateTotals() {
        // Calculate subtotal from items
        subtotal = items.stream()
                .map(SaleItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calculate total with tax and discount
        total = subtotal.add(tax).subtract(discount);
        
        this.dateModified = LocalDateTime.now();
    }
    
    /**
     * Get item count for accessibility
     */
    public int getItemCount() {
        return items.stream().mapToInt(SaleItem::getQuantity).sum();
    }
    
    /**
     * Get formatted receipt text for accessibility
     */
    public String getFormattedReceiptText() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transaction: ").append(transactionNumber).append("\n");
        sb.append("Date: ").append(saleDate.toString()).append("\n");
        sb.append("Cashier: ").append(cashier.getUsername()).append("\n\n");
        
        sb.append("Items:\n");
        for (SaleItem item : items) {
            sb.append(item.getProduct().getName())
              .append(" x").append(item.getQuantity())
              .append(" = ").append(item.getTotalPrice()).append("\n");
        }
        
        sb.append("\nSubtotal: ").append(subtotal).append("\n");
        if (tax.compareTo(BigDecimal.ZERO) > 0) {
            sb.append("Tax: ").append(tax).append("\n");
        }
        if (discount.compareTo(BigDecimal.ZERO) > 0) {
            sb.append("Discount: ").append(discount).append("\n");
        }
        sb.append("Total: ").append(total).append("\n");
        sb.append("Payment: ").append(paymentMethod).append("\n");
        
        if (accessibilityAssistanceUsed) {
            sb.append("\nAccessibility assistance was provided for this transaction.");
        }
        
        return sb.toString();
    }
    
    /**
     * Generate unique transaction number
     */
    private String generateTransactionNumber() {
        return "TXN" + System.currentTimeMillis();
    }
    
    /**
     * Void the sale
     */
    public void voidSale() {
        this.isVoided = true;
        this.dateModified = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "Sale #" + transactionNumber + " - " + getFormattedReceiptText();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Sale sale = (Sale) obj;
        return id == sale.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
