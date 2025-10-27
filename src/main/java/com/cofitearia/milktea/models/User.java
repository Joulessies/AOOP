package com.cofitearia.milktea.models;

import java.time.LocalDateTime;

/**
 * User model with role-based access control and accessibility features
 */
public class User {
    public enum Role {
        OWNER("Owner", "Full system access"),
        MANAGER("Manager", "Inventory and sales management"),
        STAFF("Staff", "Basic sales and inventory operations"),
        PWD_STAFF("PWD Staff", "Accessible interface with full staff privileges");
        
        private final String displayName;
        private final String description;
        
        Role(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    private int id;
    private String username;
    private String password; // Should be hashed in real implementation
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private LocalDateTime lastLogin;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
    private boolean isActive;
    
    // Accessibility preferences
    private boolean highContrastMode;
    private boolean largeTextMode;
    private boolean screenReaderEnabled;
    private boolean keyboardNavigationEnabled;
    private String preferredLanguage;
    
    public User() {
        this.dateCreated = LocalDateTime.now();
        this.dateModified = LocalDateTime.now();
        this.isActive = true;
        this.highContrastMode = false;
        this.largeTextMode = false;
        this.screenReaderEnabled = false;
        this.keyboardNavigationEnabled = true;
        this.preferredLanguage = "en";
    }
    
    public User(String username, String password, String firstName, String lastName, Role role) {
        this();
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
        this.dateModified = LocalDateTime.now();
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
        this.dateModified = LocalDateTime.now();
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.dateModified = LocalDateTime.now();
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.dateModified = LocalDateTime.now();
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
        this.dateModified = LocalDateTime.now();
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
        this.dateModified = LocalDateTime.now();
    }
    
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
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
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
        this.dateModified = LocalDateTime.now();
    }
    
    // Accessibility preference getters and setters
    public boolean isHighContrastMode() {
        return highContrastMode;
    }
    
    public void setHighContrastMode(boolean highContrastMode) {
        this.highContrastMode = highContrastMode;
        this.dateModified = LocalDateTime.now();
    }
    
    public boolean isLargeTextMode() {
        return largeTextMode;
    }
    
    public void setLargeTextMode(boolean largeTextMode) {
        this.largeTextMode = largeTextMode;
        this.dateModified = LocalDateTime.now();
    }
    
    public boolean isScreenReaderEnabled() {
        return screenReaderEnabled;
    }
    
    public void setScreenReaderEnabled(boolean screenReaderEnabled) {
        this.screenReaderEnabled = screenReaderEnabled;
        this.dateModified = LocalDateTime.now();
    }
    
    public boolean isKeyboardNavigationEnabled() {
        return keyboardNavigationEnabled;
    }
    
    public void setKeyboardNavigationEnabled(boolean keyboardNavigationEnabled) {
        this.keyboardNavigationEnabled = keyboardNavigationEnabled;
        this.dateModified = LocalDateTime.now();
    }
    
    public String getPreferredLanguage() {
        return preferredLanguage;
    }
    
    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
        this.dateModified = LocalDateTime.now();
    }
    
    // Business Logic Methods
    
    /**
     * Get full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    /**
     * Check if user has permission for an action
     */
    public boolean hasPermission(String action) {
        if (!isActive) return false;
        
        switch (role) {
            case OWNER:
                return true; // Owner has all permissions
            case MANAGER:
                return !action.equals("DELETE_USER") && !action.equals("SYSTEM_SETTINGS");
            case STAFF:
            case PWD_STAFF:
                return action.equals("VIEW_INVENTORY") || 
                       action.equals("PROCESS_SALE") || 
                       action.equals("VIEW_SALES") ||
                       action.equals("UPDATE_STOCK");
            default:
                return false;
        }
    }
    
    /**
     * Check if user is PWD staff
     */
    public boolean isPWDStaff() {
        return role == Role.PWD_STAFF;
    }
    
    /**
     * Get accessibility summary for user
     */
    public String getAccessibilitySummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Accessibility settings for ").append(getFullName()).append(":\n");
        sb.append("High Contrast: ").append(highContrastMode ? "Enabled" : "Disabled").append("\n");
        sb.append("Large Text: ").append(largeTextMode ? "Enabled" : "Disabled").append("\n");
        sb.append("Screen Reader: ").append(screenReaderEnabled ? "Enabled" : "Disabled").append("\n");
        sb.append("Keyboard Navigation: ").append(keyboardNavigationEnabled ? "Enabled" : "Disabled").append("\n");
        sb.append("Language: ").append(preferredLanguage);
        return sb.toString();
    }
    
    /**
     * Update last login timestamp
     */
    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
        this.dateModified = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return getFullName() + " (" + role.getDisplayName() + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return id == user.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
