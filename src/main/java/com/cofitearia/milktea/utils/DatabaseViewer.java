package com.cofitearia.milktea.utils;

import com.cofitearia.milktea.Main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Simple database viewer utility to inspect the SQLite database
 */
public class DatabaseViewer {
    
    public static void main(String[] args) {
        System.out.println("=== Cofitearia Milktea Database Viewer ===\n");
        
        try {
            // Initialize database connection
            Main.getDatabaseManager().initializeDatabase();
            
            // View all tables and data
            viewUsers();
            viewProducts();
            viewInventoryItems();
            viewSystemSettings();
            
        } catch (Exception e) {
            System.err.println("Error accessing database: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close connection
            Main.getDatabaseManager().closeConnection();
        }
    }
    
    private static void viewUsers() throws SQLException {
        System.out.println("=== USERS TABLE ===");
        String sql = "SELECT id, username, first_name, last_name, email, role, is_active, date_created FROM users";
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.printf("%-4s %-15s %-12s %-15s %-25s %-12s %-8s %-12s%n", 
                "ID", "Username", "First Name", "Last Name", "Email", "Role", "Active", "Created");
            System.out.println("-".repeat(120));
            
            while (rs.next()) {
                System.out.printf("%-4d %-15s %-12s %-15s %-25s %-12s %-8s %-12s%n",
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email") != null ? rs.getString("email") : "N/A",
                    rs.getString("role"),
                    rs.getBoolean("is_active") ? "Yes" : "No",
                    rs.getDate("date_created").toString()
                );
            }
        }
        System.out.println();
    }
    
    private static void viewProducts() throws SQLException {
        System.out.println("=== PRODUCTS TABLE ===");
        String sql = "SELECT id, name, price, category, barcode, is_active FROM products";
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.printf("%-4s %-25s %-10s %-15s %-15s %-8s%n", 
                "ID", "Name", "Price", "Category", "Barcode", "Active");
            System.out.println("-".repeat(90));
            
            while (rs.next()) {
                System.out.printf("%-4d %-25s ₱%-9.2f %-15s %-15s %-8s%n",
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getBigDecimal("price"),
                    rs.getString("category") != null ? rs.getString("category") : "N/A",
                    rs.getString("barcode") != null ? rs.getString("barcode") : "N/A",
                    rs.getBoolean("is_active") ? "Yes" : "No"
                );
            }
        }
        System.out.println();
    }
    
    private static void viewInventoryItems() throws SQLException {
        System.out.println("=== INVENTORY ITEMS TABLE ===");
        String sql = """
            SELECT ii.id, p.name as product_name, ii.current_stock, ii.minimum_stock, 
                   ii.maximum_stock, ii.expiration_date, ii.location
            FROM inventory_items ii
            JOIN products p ON ii.product_id = p.id
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.printf("%-4s %-25s %-8s %-8s %-8s %-15s %-15s%n", 
                "ID", "Product Name", "Current", "Min", "Max", "Expiration", "Location");
            System.out.println("-".repeat(100));
            
            while (rs.next()) {
                System.out.printf("%-4d %-25s %-8d %-8d %-8d %-15s %-15s%n",
                    rs.getInt("id"),
                    rs.getString("product_name"),
                    rs.getInt("current_stock"),
                    rs.getInt("minimum_stock"),
                    rs.getInt("maximum_stock"),
                    rs.getDate("expiration_date") != null ? rs.getDate("expiration_date").toString() : "N/A",
                    rs.getString("location") != null ? rs.getString("location") : "N/A"
                );
            }
        }
        System.out.println();
    }
    
    private static void viewSystemSettings() throws SQLException {
        System.out.println("=== SYSTEM SETTINGS TABLE ===");
        String sql = "SELECT setting_key, setting_value, description FROM system_settings";
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.printf("%-20s %-15s %-30s%n", "Setting Key", "Value", "Description");
            System.out.println("-".repeat(70));
            
            while (rs.next()) {
                System.out.printf("%-20s %-15s %-30s%n",
                    rs.getString("setting_key"),
                    rs.getString("setting_value"),
                    rs.getString("description")
                );
            }
        }
        System.out.println();
    }
    
    public static void viewDatabaseStats() {
        try {
            Main.getDatabaseManager().initializeDatabase();
            System.out.println(Main.getDatabaseManager().getDatabaseStats());
            Main.getDatabaseManager().closeConnection();
        } catch (Exception e) {
            System.err.println("Error getting database stats: " + e.getMessage());
        }
    }
}
