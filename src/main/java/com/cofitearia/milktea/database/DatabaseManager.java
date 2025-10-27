package com.cofitearia.milktea.database;

import java.sql.*;
import java.util.logging.Logger;

/**
 * Database manager for handling SQLite database operations
 * Includes accessibility logging and error handling
 */
public class DatabaseManager {
    private static final Logger logger = Logger.getLogger(DatabaseManager.class.getName());
    private static final String DB_URL = "jdbc:sqlite:milktea_inventory.db";
    private Connection connection;
    
    public DatabaseManager() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            logger.severe("SQLite JDBC driver not found: " + e.getMessage());
            throw new RuntimeException("Database driver not available", e);
        }
    }
    
    /**
     * Initialize database connection and create tables
     */
    public void initializeDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            createTables();
            insertDefaultData();
            logger.info("Database initialized successfully");
        } catch (SQLException e) {
            logger.severe("Failed to initialize database: " + e.getMessage());
            throw new RuntimeException("Database initialization failed", e);
        }
    }
    
    /**
     * Create all necessary tables
     */
    private void createTables() throws SQLException {
        String[] createTableStatements = {
            // Users table
            """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                email TEXT,
                role TEXT NOT NULL CHECK(role IN ('OWNER', 'MANAGER', 'STAFF', 'PWD_STAFF')),
                last_login DATETIME,
                date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
                date_modified DATETIME DEFAULT CURRENT_TIMESTAMP,
                is_active BOOLEAN DEFAULT 1,
                high_contrast_mode BOOLEAN DEFAULT 0,
                large_text_mode BOOLEAN DEFAULT 0,
                screen_reader_enabled BOOLEAN DEFAULT 0,
                keyboard_navigation_enabled BOOLEAN DEFAULT 1,
                preferred_language TEXT DEFAULT 'en'
            )
            """,
            
            // Products table
            """
            CREATE TABLE IF NOT EXISTS products (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                description TEXT,
                price DECIMAL(10,2) NOT NULL,
                category TEXT,
                barcode TEXT UNIQUE,
                unit TEXT DEFAULT 'piece',
                date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
                date_modified DATETIME DEFAULT CURRENT_TIMESTAMP,
                is_active BOOLEAN DEFAULT 1,
                alt_text TEXT,
                large_text_description TEXT
            )
            """,
            
            // Inventory items table
            """
            CREATE TABLE IF NOT EXISTS inventory_items (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                product_id INTEGER NOT NULL,
                current_stock INTEGER NOT NULL DEFAULT 0,
                minimum_stock INTEGER NOT NULL DEFAULT 0,
                maximum_stock INTEGER DEFAULT 1000,
                cost_price DECIMAL(10,2),
                expiration_date DATE,
                supplier TEXT,
                location TEXT,
                last_restocked DATE,
                date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
                date_modified DATETIME DEFAULT CURRENT_TIMESTAMP,
                is_active BOOLEAN DEFAULT 1,
                low_stock_threshold INTEGER DEFAULT 10,
                critical_stock_threshold INTEGER DEFAULT 5,
                FOREIGN KEY (product_id) REFERENCES products (id)
            )
            """,
            
            // Sales table
            """
            CREATE TABLE IF NOT EXISTS sales (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                transaction_number TEXT UNIQUE NOT NULL,
                subtotal DECIMAL(10,2) NOT NULL DEFAULT 0,
                tax DECIMAL(10,2) DEFAULT 0,
                discount DECIMAL(10,2) DEFAULT 0,
                total DECIMAL(10,2) NOT NULL DEFAULT 0,
                payment_method TEXT,
                customer_info TEXT,
                cashier_id INTEGER,
                sale_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
                date_modified DATETIME DEFAULT CURRENT_TIMESTAMP,
                notes TEXT,
                is_voided BOOLEAN DEFAULT 0,
                accessibility_assistance_used BOOLEAN DEFAULT 0,
                accessibility_notes TEXT,
                FOREIGN KEY (cashier_id) REFERENCES users (id)
            )
            """,
            
            // Sale items table
            """
            CREATE TABLE IF NOT EXISTS sale_items (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                sale_id INTEGER NOT NULL,
                product_id INTEGER NOT NULL,
                quantity INTEGER NOT NULL,
                unit_price DECIMAL(10,2) NOT NULL,
                total_price DECIMAL(10,2) NOT NULL,
                notes TEXT,
                FOREIGN KEY (sale_id) REFERENCES sales (id),
                FOREIGN KEY (product_id) REFERENCES products (id)
            )
            """,
            
            // Stock movements table for audit trail
            """
            CREATE TABLE IF NOT EXISTS stock_movements (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                inventory_item_id INTEGER NOT NULL,
                movement_type TEXT NOT NULL CHECK(movement_type IN ('IN', 'OUT', 'ADJUSTMENT')),
                quantity INTEGER NOT NULL,
                reason TEXT,
                user_id INTEGER,
                date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (inventory_item_id) REFERENCES inventory_items (id),
                FOREIGN KEY (user_id) REFERENCES users (id)
            )
            """,
            
            // System settings table
            """
            CREATE TABLE IF NOT EXISTS system_settings (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                setting_key TEXT UNIQUE NOT NULL,
                setting_value TEXT,
                description TEXT,
                date_modified DATETIME DEFAULT CURRENT_TIMESTAMP
            )
            """
        };
        
        try (Statement stmt = connection.createStatement()) {
            for (String sql : createTableStatements) {
                stmt.execute(sql);
            }
        }
        
        // Create indexes for better performance
        createIndexes();
    }
    
    /**
     * Create database indexes for better performance
     */
    private void createIndexes() throws SQLException {
        String[] indexStatements = {
            "CREATE INDEX IF NOT EXISTS idx_users_username ON users(username)",
            "CREATE INDEX IF NOT EXISTS idx_products_barcode ON products(barcode)",
            "CREATE INDEX IF NOT EXISTS idx_products_category ON products(category)",
            "CREATE INDEX IF NOT EXISTS idx_inventory_product ON inventory_items(product_id)",
            "CREATE INDEX IF NOT EXISTS idx_sales_date ON sales(sale_date)",
            "CREATE INDEX IF NOT EXISTS idx_sales_cashier ON sales(cashier_id)",
            "CREATE INDEX IF NOT EXISTS idx_sale_items_sale ON sale_items(sale_id)",
            "CREATE INDEX IF NOT EXISTS idx_stock_movements_inventory ON stock_movements(inventory_item_id)"
        };
        
        try (Statement stmt = connection.createStatement()) {
            for (String sql : indexStatements) {
                stmt.execute(sql);
            }
        }
    }
    
    /**
     * Insert default data (admin user, sample products)
     */
    private void insertDefaultData() throws SQLException {
        // Check if default data already exists
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT COUNT(*) FROM users WHERE username = ?")) {
            stmt.setString(1, "admin");
            ResultSet rs = stmt.executeQuery();
            if (rs.getInt(1) > 0) {
                return; // Default data already exists
            }
        }
        
        // Insert default admin user
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO users (username, password, first_name, last_name, role) VALUES (?, ?, ?, ?, ?)")) {
            stmt.setString(1, "admin");
            stmt.setString(2, "admin123"); // In production, this should be hashed
            stmt.setString(3, "System");
            stmt.setString(4, "Administrator");
            stmt.setString(5, "OWNER");
            int rowsAffected = stmt.executeUpdate();
            logger.info("Default admin user inserted. Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            logger.severe("Error inserting default admin user: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Insert sample products
        String[] sampleProducts = {
            "INSERT INTO products (name, description, price, category) VALUES ('Classic Milk Tea', 'Original milk tea with tapioca pearls', 45.00, 'Beverages')",
            "INSERT INTO products (name, description, price, category) VALUES ('Taro Milk Tea', 'Purple taro flavored milk tea', 50.00, 'Beverages')",
            "INSERT INTO products (name, description, price, category) VALUES ('Matcha Latte', 'Green tea matcha with steamed milk', 55.00, 'Beverages')",
            "INSERT INTO products (name, description, price, category) VALUES ('Tapioca Pearls', 'Black tapioca pearls for milk tea', 15.00, 'Add-ons')",
            "INSERT INTO products (name, description, price, category) VALUES ('Jelly', 'Fruit jelly cubes', 12.00, 'Add-ons')"
        };
        
        try (Statement stmt = connection.createStatement()) {
            for (String sql : sampleProducts) {
                stmt.execute(sql);
            }
        }
        
        // Insert inventory for sample products
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO inventory_items (product_id, current_stock, minimum_stock, maximum_stock) VALUES (?, ?, ?, ?)")) {
            for (int i = 1; i <= 5; i++) {
                stmt.setInt(1, i);
                stmt.setInt(2, 100); // current stock
                stmt.setInt(3, 20);  // minimum stock
                stmt.setInt(4, 500); // maximum stock
                stmt.executeUpdate();
            }
        }
        
        // Insert default system settings
        String[] settings = {
            "INSERT INTO system_settings (setting_key, setting_value, description) VALUES ('tax_rate', '0.12', 'Default tax rate (12%)')",
            "INSERT INTO system_settings (setting_key, setting_value, description) VALUES ('currency_symbol', '₱', 'Currency symbol')",
            "INSERT INTO system_settings (setting_key, setting_value, description) VALUES ('accessibility_enabled', 'true', 'Enable accessibility features')"
        };
        
        try (Statement stmt = connection.createStatement()) {
            for (String sql : settings) {
                stmt.execute(sql);
            }
        }
        
        logger.info("Default data inserted successfully");
    }
    
    /**
     * Get database connection
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }
    
    /**
     * Close database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Database connection closed");
            }
        } catch (SQLException e) {
            logger.severe("Error closing database connection: " + e.getMessage());
        }
    }
    
    /**
     * Test database connection
     */
    public boolean testConnection() {
        try {
            Connection testConn = getConnection();
            return testConn != null && !testConn.isClosed();
        } catch (SQLException e) {
            logger.severe("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get database statistics for monitoring
     */
    public String getDatabaseStats() {
        try (Connection conn = getConnection()) {
            StringBuilder stats = new StringBuilder();
            stats.append("Database Statistics:\n");
            
            String[] tables = {"users", "products", "inventory_items", "sales", "sale_items", "stock_movements"};
            
            for (String table : tables) {
                try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM " + table)) {
                    ResultSet rs = stmt.executeQuery();
                    stats.append(table).append(": ").append(rs.getInt(1)).append(" records\n");
                }
            }
            
            return stats.toString();
        } catch (SQLException e) {
            logger.severe("Error getting database stats: " + e.getMessage());
            return "Error retrieving database statistics";
        }
    }
}
