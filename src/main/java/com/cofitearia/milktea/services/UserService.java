package com.cofitearia.milktea.services;

import com.cofitearia.milktea.Main;
import com.cofitearia.milktea.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * User service for handling user authentication and management
 * Includes accessibility features and PWD user support
 */
public class UserService {
    private static final Logger logger = Logger.getLogger(UserService.class.getName());
    
    /**
     * Authenticate user with username and password
     */
    public User authenticateUser(String username, String password) {
        String sql = """
            SELECT id, username, password, first_name, last_name, email, role,
                   last_login, date_created, date_modified, is_active,
                   high_contrast_mode, large_text_mode, screen_reader_enabled,
                   keyboard_navigation_enabled, preferred_language
            FROM users 
            WHERE username = ? AND password = ? AND is_active = 1
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password); // In production, use hashed passwords
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = mapResultSetToUser(rs);
                    logger.info("User authenticated successfully: " + username);
                    return user;
                } else {
                    logger.warning("No user found with username: " + username + " and password: " + password);
                    // Let's check if the user exists at all
                    checkUserExists(username);
                }
            }
        } catch (SQLException e) {
            logger.severe("Authentication error for user " + username + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        logger.warning("Authentication failed for user: " + username);
        return null;
    }
    
    /**
     * Get user by ID
     */
    public User getUserById(int userId) {
        String sql = """
            SELECT id, username, password, first_name, last_name, email, role,
                   last_login, date_created, date_modified, is_active,
                   high_contrast_mode, large_text_mode, screen_reader_enabled,
                   keyboard_navigation_enabled, preferred_language
            FROM users 
            WHERE id = ? AND is_active = 1
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting user by ID " + userId + ": " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = """
            SELECT id, username, password, first_name, last_name, email, role,
                   last_login, date_created, date_modified, is_active,
                   high_contrast_mode, large_text_mode, screen_reader_enabled,
                   keyboard_navigation_enabled, preferred_language
            FROM users 
            WHERE is_active = 1
            ORDER BY last_name, first_name
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            logger.severe("Error getting all users: " + e.getMessage());
        }
        
        return users;
    }
    
    /**
     * Create new user
     */
    public boolean createUser(User user) {
        String sql = """
            INSERT INTO users (username, password, first_name, last_name, email, role,
                              high_contrast_mode, large_text_mode, screen_reader_enabled,
                              keyboard_navigation_enabled, preferred_language)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword()); // In production, hash the password
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getRole().toString());
            stmt.setBoolean(7, user.isHighContrastMode());
            stmt.setBoolean(8, user.isLargeTextMode());
            stmt.setBoolean(9, user.isScreenReaderEnabled());
            stmt.setBoolean(10, user.isKeyboardNavigationEnabled());
            stmt.setString(11, user.getPreferredLanguage());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("User created successfully: " + user.getUsername());
                return true;
            }
        } catch (SQLException e) {
            logger.severe("Error creating user " + user.getUsername() + ": " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Update user
     */
    public boolean updateUser(User user) {
        String sql = """
            UPDATE users 
            SET username = ?, first_name = ?, last_name = ?, email = ?, role = ?,
                high_contrast_mode = ?, large_text_mode = ?, screen_reader_enabled = ?,
                keyboard_navigation_enabled = ?, preferred_language = ?,
                date_modified = CURRENT_TIMESTAMP
            WHERE id = ?
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getFirstName());
            stmt.setString(3, user.getLastName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getRole().toString());
            stmt.setBoolean(6, user.isHighContrastMode());
            stmt.setBoolean(7, user.isLargeTextMode());
            stmt.setBoolean(8, user.isScreenReaderEnabled());
            stmt.setBoolean(9, user.isKeyboardNavigationEnabled());
            stmt.setString(10, user.getPreferredLanguage());
            stmt.setInt(11, user.getId());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("User updated successfully: " + user.getUsername());
                return true;
            }
        } catch (SQLException e) {
            logger.severe("Error updating user " + user.getUsername() + ": " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Update user's last login timestamp
     */
    public void updateLastLogin(User user) {
        String sql = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, user.getId());
            stmt.executeUpdate();
            
            user.setLastLogin(LocalDateTime.now());
            logger.info("Last login updated for user: " + user.getUsername());
            
        } catch (SQLException e) {
            logger.severe("Error updating last login for user " + user.getUsername() + ": " + e.getMessage());
        }
    }
    
    /**
     * Deactivate user (soft delete)
     */
    public boolean deactivateUser(int userId) {
        String sql = "UPDATE users SET is_active = 0, date_modified = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("User deactivated successfully: ID " + userId);
                return true;
            }
        } catch (SQLException e) {
            logger.severe("Error deactivating user ID " + userId + ": " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Check if username exists
     */
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND is_active = 1";
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.severe("Error checking username existence: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Get users by role
     */
    public List<User> getUsersByRole(User.Role role) {
        List<User> users = new ArrayList<>();
        String sql = """
            SELECT id, username, password, first_name, last_name, email, role,
                   last_login, date_created, date_modified, is_active,
                   high_contrast_mode, large_text_mode, screen_reader_enabled,
                   keyboard_navigation_enabled, preferred_language
            FROM users 
            WHERE role = ? AND is_active = 1
            ORDER BY last_name, first_name
            """;
        
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, role.toString());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting users by role " + role + ": " + e.getMessage());
        }
        
        return users;
    }
    
    /**
     * Check if user exists in database (for debugging)
     */
    private void checkUserExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    logger.info("User '" + username + "' exists in database: " + (count > 0));
                    if (count > 0) {
                        // Let's also check the password
                        checkUserPassword(username);
                    }
                }
            }
        } catch (SQLException e) {
            logger.severe("Error checking if user exists: " + e.getMessage());
        }
    }
    
    /**
     * Check user password (for debugging)
     */
    private void checkUserPassword(String username) {
        String sql = "SELECT password, is_active FROM users WHERE username = ?";
        try (Connection conn = Main.getDatabaseManager().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    boolean isActive = rs.getBoolean("is_active");
                    logger.info("User '" + username + "' - Password: '" + storedPassword + "', Active: " + isActive);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error checking user password: " + e.getMessage());
        }
    }
    
    /**
     * Map ResultSet to User object
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        user.setRole(User.Role.valueOf(rs.getString("role")));
        user.setLastLogin(rs.getTimestamp("last_login") != null ? 
                         rs.getTimestamp("last_login").toLocalDateTime() : null);
        user.setDateCreated(rs.getTimestamp("date_created").toLocalDateTime());
        user.setDateModified(rs.getTimestamp("date_modified").toLocalDateTime());
        user.setActive(rs.getBoolean("is_active"));
        
        // Accessibility preferences
        user.setHighContrastMode(rs.getBoolean("high_contrast_mode"));
        user.setLargeTextMode(rs.getBoolean("large_text_mode"));
        user.setScreenReaderEnabled(rs.getBoolean("screen_reader_enabled"));
        user.setKeyboardNavigationEnabled(rs.getBoolean("keyboard_navigation_enabled"));
        user.setPreferredLanguage(rs.getString("preferred_language"));
        
        return user;
    }
}
