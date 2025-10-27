package com.cofitearia.milktea.services;

import com.cofitearia.milktea.Main;
import com.cofitearia.milktea.models.Product;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Product service for handling product management operations
 */
public class ProductService {
    private static final Logger logger = Logger.getLogger(ProductService.class.getName());
    
    /**
     * Get all active products
     */
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = """
            SELECT id, name, description, price, category, barcode, unit,
                   date_created, date_modified, is_active, alt_text, large_text_description
            FROM products 
            WHERE is_active = 1
            ORDER BY name
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            logger.severe("Error getting all products: " + e.getMessage());
        }
        
        return products;
    }
    
    /**
     * Get product by ID
     */
    public Product getProductById(int productId) {
        String sql = """
            SELECT id, name, description, price, category, barcode, unit,
                   date_created, date_modified, is_active, alt_text, large_text_description
            FROM products 
            WHERE id = ? AND is_active = 1
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, productId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting product by ID " + productId + ": " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get products by category
     */
    public List<Product> getProductsByCategory(String category) {
        List<Product> products = new ArrayList<>();
        String sql = """
            SELECT id, name, description, price, category, barcode, unit,
                   date_created, date_modified, is_active, alt_text, large_text_description
            FROM products 
            WHERE category = ? AND is_active = 1
            ORDER BY name
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, category);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting products by category " + category + ": " + e.getMessage());
        }
        
        return products;
    }
    
    /**
     * Create new product
     */
    public boolean createProduct(Product product) {
        String sql = """
            INSERT INTO products (name, description, price, category, barcode, unit, alt_text, large_text_description)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setString(4, product.getCategory());
            stmt.setString(5, product.getBarcode());
            stmt.setString(6, product.getUnit());
            stmt.setString(7, product.getAltText());
            stmt.setString(8, product.getLargeTextDescription());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("Product created successfully: " + product.getName());
                return true;
            }
        } catch (SQLException e) {
            logger.severe("Error creating product " + product.getName() + ": " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Update product
     */
    public boolean updateProduct(Product product) {
        String sql = """
            UPDATE products 
            SET name = ?, description = ?, price = ?, category = ?, barcode = ?, unit = ?,
                alt_text = ?, large_text_description = ?, date_modified = CURRENT_TIMESTAMP
            WHERE id = ?
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setString(4, product.getCategory());
            stmt.setString(5, product.getBarcode());
            stmt.setString(6, product.getUnit());
            stmt.setString(7, product.getAltText());
            stmt.setString(8, product.getLargeTextDescription());
            stmt.setInt(9, product.getId());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("Product updated successfully: " + product.getName());
                return true;
            }
        } catch (SQLException e) {
            logger.severe("Error updating product " + product.getName() + ": " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Delete product (soft delete)
     */
    public boolean deleteProduct(int productId) {
        String sql = "UPDATE products SET is_active = 0, date_modified = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, productId);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("Product deleted successfully: ID " + productId);
                return true;
            }
        } catch (SQLException e) {
            logger.severe("Error deleting product ID " + productId + ": " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Search products by name
     */
    public List<Product> searchProducts(String searchTerm) {
        List<Product> products = new ArrayList<>();
        String sql = """
            SELECT id, name, description, price, category, barcode, unit,
                   date_created, date_modified, is_active, alt_text, large_text_description
            FROM products 
            WHERE (name LIKE ? OR description LIKE ? OR barcode LIKE ?) AND is_active = 1
            ORDER BY name
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            logger.severe("Error searching products with term '" + searchTerm + "': " + e.getMessage());
        }
        
        return products;
    }
    
    /**
     * Get all categories
     */
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM products WHERE is_active = 1 AND category IS NOT NULL ORDER BY category";
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            logger.severe("Error getting categories: " + e.getMessage());
        }
        
        return categories;
    }
    
    /**
     * Check if barcode exists
     */
    public boolean barcodeExists(String barcode) {
        String sql = "SELECT COUNT(*) FROM products WHERE barcode = ? AND is_active = 1";
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, barcode);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.severe("Error checking barcode existence: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Map ResultSet to Product object
     */
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setCategory(rs.getString("category"));
        product.setBarcode(rs.getString("barcode"));
        product.setUnit(rs.getString("unit"));
        product.setDateCreated(rs.getDate("date_created").toLocalDate());
        product.setDateModified(rs.getDate("date_modified").toLocalDate());
        product.setActive(rs.getBoolean("is_active"));
        product.setAltText(rs.getString("alt_text"));
        product.setLargeTextDescription(rs.getString("large_text_description"));
        
        return product;
    }
}
