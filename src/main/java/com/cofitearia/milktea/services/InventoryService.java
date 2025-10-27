package com.cofitearia.milktea.services;

import com.cofitearia.milktea.Main;
import com.cofitearia.milktea.models.InventoryItem;
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
 * Inventory service for handling inventory management operations
 * Includes low stock alerts and automated reordering features
 */
public class InventoryService {
    private static final Logger logger = Logger.getLogger(InventoryService.class.getName());
    
    /**
     * Get all inventory items
     */
    public List<InventoryItem> getAllInventoryItems() {
        List<InventoryItem> items = new ArrayList<>();
        String sql = """
            SELECT ii.id, ii.product_id, ii.current_stock, ii.minimum_stock, ii.maximum_stock,
                   ii.cost_price, ii.expiration_date, ii.supplier, ii.location, ii.last_restocked,
                   ii.date_created, ii.date_modified, ii.is_active, ii.low_stock_threshold, ii.critical_stock_threshold,
                   p.name, p.description, p.price, p.category, p.barcode, p.unit
            FROM inventory_items ii
            JOIN products p ON ii.product_id = p.id
            WHERE ii.is_active = 1 AND p.is_active = 1
            ORDER BY p.name
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                items.add(mapResultSetToInventoryItem(rs));
            }
        } catch (SQLException e) {
            logger.severe("Error getting all inventory items: " + e.getMessage());
        }
        
        return items;
    }
    
    /**
     * Get inventory item by ID
     */
    public InventoryItem getInventoryItemById(int itemId) {
        String sql = """
            SELECT ii.id, ii.product_id, ii.current_stock, ii.minimum_stock, ii.maximum_stock,
                   ii.cost_price, ii.expiration_date, ii.supplier, ii.location, ii.last_restocked,
                   ii.date_created, ii.date_modified, ii.is_active, ii.low_stock_threshold, ii.critical_stock_threshold,
                   p.name, p.description, p.price, p.category, p.barcode, p.unit
            FROM inventory_items ii
            JOIN products p ON ii.product_id = p.id
            WHERE ii.id = ? AND ii.is_active = 1
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, itemId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInventoryItem(rs);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting inventory item by ID " + itemId + ": " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get inventory item by product ID
     */
    public InventoryItem getInventoryItemByProductId(int productId) {
        String sql = """
            SELECT ii.id, ii.product_id, ii.current_stock, ii.minimum_stock, ii.maximum_stock,
                   ii.cost_price, ii.expiration_date, ii.supplier, ii.location, ii.last_restocked,
                   ii.date_created, ii.date_modified, ii.is_active, ii.low_stock_threshold, ii.critical_stock_threshold,
                   p.name, p.description, p.price, p.category, p.barcode, p.unit
            FROM inventory_items ii
            JOIN products p ON ii.product_id = p.id
            WHERE ii.product_id = ? AND ii.is_active = 1
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, productId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInventoryItem(rs);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting inventory item by product ID " + productId + ": " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Create new inventory item
     */
    public boolean createInventoryItem(InventoryItem item) {
        String sql = """
            INSERT INTO inventory_items (product_id, current_stock, minimum_stock, maximum_stock,
                                        cost_price, expiration_date, supplier, location,
                                        low_stock_threshold, critical_stock_threshold)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, item.getProduct().getId());
            stmt.setInt(2, item.getCurrentStock());
            stmt.setInt(3, item.getMinimumStock());
            stmt.setInt(4, item.getMaximumStock());
            stmt.setBigDecimal(5, item.getCostPrice());
            stmt.setDate(6, item.getExpirationDate() != null ? 
                         java.sql.Date.valueOf(item.getExpirationDate()) : null);
            stmt.setString(7, item.getSupplier());
            stmt.setString(8, item.getLocation());
            stmt.setInt(9, item.getLowStockThreshold());
            stmt.setInt(10, item.getCriticalStockThreshold());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("Inventory item created successfully for product: " + item.getProduct().getName());
                return true;
            }
        } catch (SQLException e) {
            logger.severe("Error creating inventory item: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Update inventory item
     */
    public boolean updateInventoryItem(InventoryItem item) {
        String sql = """
            UPDATE inventory_items 
            SET current_stock = ?, minimum_stock = ?, maximum_stock = ?, cost_price = ?,
                expiration_date = ?, supplier = ?, location = ?, last_restocked = ?,
                low_stock_threshold = ?, critical_stock_threshold = ?, date_modified = CURRENT_TIMESTAMP
            WHERE id = ?
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, item.getCurrentStock());
            stmt.setInt(2, item.getMinimumStock());
            stmt.setInt(3, item.getMaximumStock());
            stmt.setBigDecimal(4, item.getCostPrice());
            stmt.setDate(5, item.getExpirationDate() != null ? 
                         java.sql.Date.valueOf(item.getExpirationDate()) : null);
            stmt.setString(6, item.getSupplier());
            stmt.setString(7, item.getLocation());
            stmt.setDate(8, item.getLastRestocked() != null ? 
                         java.sql.Date.valueOf(item.getLastRestocked()) : null);
            stmt.setInt(9, item.getLowStockThreshold());
            stmt.setInt(10, item.getCriticalStockThreshold());
            stmt.setInt(11, item.getId());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("Inventory item updated successfully: " + item.getProduct().getName());
                return true;
            }
        } catch (SQLException e) {
            logger.severe("Error updating inventory item: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Add stock to inventory item
     */
    public boolean addStock(int itemId, int quantity, String reason) {
        String sql = """
            UPDATE inventory_items 
            SET current_stock = current_stock + ?, last_restocked = CURRENT_DATE, date_modified = CURRENT_TIMESTAMP
            WHERE id = ?
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantity);
            stmt.setInt(2, itemId);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Record stock movement
                recordStockMovement(itemId, "IN", quantity, reason, null);
                logger.info("Stock added successfully: " + quantity + " units to item ID " + itemId);
                return true;
            }
        } catch (SQLException e) {
            logger.severe("Error adding stock to item ID " + itemId + ": " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Remove stock from inventory item
     */
    public boolean removeStock(int itemId, int quantity, String reason) {
        String sql = """
            UPDATE inventory_items 
            SET current_stock = current_stock - ?, date_modified = CURRENT_TIMESTAMP
            WHERE id = ? AND current_stock >= ?
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantity);
            stmt.setInt(2, itemId);
            stmt.setInt(3, quantity);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Record stock movement
                recordStockMovement(itemId, "OUT", quantity, reason, null);
                logger.info("Stock removed successfully: " + quantity + " units from item ID " + itemId);
                return true;
            }
        } catch (SQLException e) {
            logger.severe("Error removing stock from item ID " + itemId + ": " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Get low stock items
     */
    public List<InventoryItem> getLowStockItems() {
        List<InventoryItem> items = new ArrayList<>();
        String sql = """
            SELECT ii.id, ii.product_id, ii.current_stock, ii.minimum_stock, ii.maximum_stock,
                   ii.cost_price, ii.expiration_date, ii.supplier, ii.location, ii.last_restocked,
                   ii.date_created, ii.date_modified, ii.is_active, ii.low_stock_threshold, ii.critical_stock_threshold,
                   p.name, p.description, p.price, p.category, p.barcode, p.unit
            FROM inventory_items ii
            JOIN products p ON ii.product_id = p.id
            WHERE ii.current_stock <= ii.low_stock_threshold AND ii.is_active = 1
            ORDER BY ii.current_stock ASC
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                items.add(mapResultSetToInventoryItem(rs));
            }
        } catch (SQLException e) {
            logger.severe("Error getting low stock items: " + e.getMessage());
        }
        
        return items;
    }
    
    /**
     * Get critical stock items
     */
    public List<InventoryItem> getCriticalStockItems() {
        List<InventoryItem> items = new ArrayList<>();
        String sql = """
            SELECT ii.id, ii.product_id, ii.current_stock, ii.minimum_stock, ii.maximum_stock,
                   ii.cost_price, ii.expiration_date, ii.supplier, ii.location, ii.last_restocked,
                   ii.date_created, ii.date_modified, ii.is_active, ii.low_stock_threshold, ii.critical_stock_threshold,
                   p.name, p.description, p.price, p.category, p.barcode, p.unit
            FROM inventory_items ii
            JOIN products p ON ii.product_id = p.id
            WHERE ii.current_stock <= ii.critical_stock_threshold AND ii.is_active = 1
            ORDER BY ii.current_stock ASC
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                items.add(mapResultSetToInventoryItem(rs));
            }
        } catch (SQLException e) {
            logger.severe("Error getting critical stock items: " + e.getMessage());
        }
        
        return items;
    }
    
    /**
     * Get expired items
     */
    public List<InventoryItem> getExpiredItems() {
        List<InventoryItem> items = new ArrayList<>();
        String sql = """
            SELECT ii.id, ii.product_id, ii.current_stock, ii.minimum_stock, ii.maximum_stock,
                   ii.cost_price, ii.expiration_date, ii.supplier, ii.location, ii.last_restocked,
                   ii.date_created, ii.date_modified, ii.is_active, ii.low_stock_threshold, ii.critical_stock_threshold,
                   p.name, p.description, p.price, p.category, p.barcode, p.unit
            FROM inventory_items ii
            JOIN products p ON ii.product_id = p.id
            WHERE ii.expiration_date < CURRENT_DATE AND ii.is_active = 1 AND ii.current_stock > 0
            ORDER BY ii.expiration_date ASC
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                items.add(mapResultSetToInventoryItem(rs));
            }
        } catch (SQLException e) {
            logger.severe("Error getting expired items: " + e.getMessage());
        }
        
        return items;
    }
    
    /**
     * Record stock movement for audit trail
     */
    private void recordStockMovement(int itemId, String movementType, int quantity, String reason, Integer userId) {
        String sql = """
            INSERT INTO stock_movements (inventory_item_id, movement_type, quantity, reason, user_id)
            VALUES (?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, itemId);
            stmt.setString(2, movementType);
            stmt.setInt(3, quantity);
            stmt.setString(4, reason);
            stmt.setObject(5, userId);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Error recording stock movement: " + e.getMessage());
        }
    }
    
    /**
     * Map ResultSet to InventoryItem object
     */
    private InventoryItem mapResultSetToInventoryItem(ResultSet rs) throws SQLException {
        InventoryItem item = new InventoryItem();
        
        item.setId(rs.getInt("id"));
        item.setCurrentStock(rs.getInt("current_stock"));
        item.setMinimumStock(rs.getInt("minimum_stock"));
        item.setMaximumStock(rs.getInt("maximum_stock"));
        item.setCostPrice(rs.getBigDecimal("cost_price"));
        item.setExpirationDate(rs.getDate("expiration_date") != null ? 
                              rs.getDate("expiration_date").toLocalDate() : null);
        item.setSupplier(rs.getString("supplier"));
        item.setLocation(rs.getString("location"));
        item.setLastRestocked(rs.getDate("last_restocked") != null ? 
                             rs.getDate("last_restocked").toLocalDate() : null);
        item.setDateCreated(rs.getDate("date_created").toLocalDate());
        item.setDateModified(rs.getDate("date_modified").toLocalDate());
        item.setActive(rs.getBoolean("is_active"));
        item.setLowStockThreshold(rs.getInt("low_stock_threshold"));
        item.setCriticalStockThreshold(rs.getInt("critical_stock_threshold"));
        
        // Create and set product
        Product product = new Product();
        product.setId(rs.getInt("product_id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setCategory(rs.getString("category"));
        product.setBarcode(rs.getString("barcode"));
        product.setUnit(rs.getString("unit"));
        item.setProduct(product);
        
        return item;
    }
}
