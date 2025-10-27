package com.cofitearia.milktea.utils;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Accessibility manager for handling PWD-friendly features
 * Includes high contrast mode, large text, keyboard navigation, and screen reader support
 */
public class AccessibilityManager {
    private static final Logger logger = Logger.getLogger(AccessibilityManager.class.getName());
    
    private boolean highContrastMode = false;
    private boolean largeTextMode = false;
    private boolean screenReaderEnabled = false;
    private boolean keyboardNavigationEnabled = true;
    private String currentLanguage = "en";
    
    // Accessibility color schemes
    private final Map<String, Color> normalColors = new HashMap<>();
    private final Map<String, Color> highContrastColors = new HashMap<>();
    
    public AccessibilityManager() {
        initializeColorSchemes();
    }
    
    /**
     * Initialize color schemes for normal and high contrast modes
     */
    private void initializeColorSchemes() {
        // Normal color scheme
        normalColors.put("background", Color.valueOf("#f5f5f5"));
        normalColors.put("primary", Color.valueOf("#2196F3"));
        normalColors.put("secondary", Color.valueOf("#FFC107"));
        normalColors.put("success", Color.valueOf("#4CAF50"));
        normalColors.put("warning", Color.valueOf("#FF9800"));
        normalColors.put("error", Color.valueOf("#F44336"));
        normalColors.put("text", Color.valueOf("#333333"));
        normalColors.put("textSecondary", Color.valueOf("#666666"));
        
        // High contrast color scheme
        highContrastColors.put("background", Color.valueOf("#000000"));
        highContrastColors.put("primary", Color.valueOf("#FFFFFF"));
        highContrastColors.put("secondary", Color.valueOf("#FFFF00"));
        highContrastColors.put("success", Color.valueOf("#00FF00"));
        highContrastColors.put("warning", Color.valueOf("#FF8000"));
        highContrastColors.put("error", Color.valueOf("#FF0000"));
        highContrastColors.put("text", Color.valueOf("#FFFFFF"));
        highContrastColors.put("textSecondary", Color.valueOf("#CCCCCC"));
    }
    
    /**
     * Initialize accessibility features
     */
    public void initializeAccessibility() {
        // Enable accessibility features by default for better user experience
        keyboardNavigationEnabled = true;
        
        // Check system accessibility settings if available
        try {
            // This would integrate with Java Access Bridge in a real implementation
            checkSystemAccessibilitySettings();
        } catch (Exception e) {
            logger.warning("Could not detect system accessibility settings: " + e.getMessage());
        }
        
        logger.info("Accessibility manager initialized");
    }
    
    /**
     * Apply accessibility settings to a scene
     */
    public void applyAccessibilitySettings(Scene scene) {
        if (highContrastMode) {
            applyHighContrastMode(scene);
        }
        
        if (largeTextMode) {
            applyLargeTextMode(scene);
        }
        
        if (keyboardNavigationEnabled) {
            enableKeyboardNavigation(scene);
        }
        
        // Set focus traversal for better keyboard navigation
        scene.getRoot().setFocusTraversable(true);
        
        logger.info("Accessibility settings applied to scene");
    }
    
    /**
     * Toggle high contrast mode
     */
    public void toggleHighContrastMode() {
        highContrastMode = !highContrastMode;
        logger.info("High contrast mode: " + (highContrastMode ? "enabled" : "disabled"));
    }
    
    /**
     * Toggle large text mode
     */
    public void toggleLargeTextMode() {
        largeTextMode = !largeTextMode;
        logger.info("Large text mode: " + (largeTextMode ? "enabled" : "disabled"));
    }
    
    /**
     * Toggle accessibility mode (combination of features)
     */
    public void toggleAccessibilityMode() {
        highContrastMode = !highContrastMode;
        largeTextMode = !largeTextMode;
        screenReaderEnabled = !screenReaderEnabled;
        
        logger.info("Accessibility mode toggled - High contrast: " + highContrastMode + 
                   ", Large text: " + largeTextMode + ", Screen reader: " + screenReaderEnabled);
    }
    
    /**
     * Apply high contrast mode to scene
     */
    private void applyHighContrastMode(Scene scene) {
        Map<String, Color> colors = highContrastMode ? highContrastColors : normalColors;
        
        // Apply high contrast stylesheet
        String highContrastCSS = """
            .root {
                -fx-base: %s;
                -fx-background: %s;
                -fx-control-inner-background: %s;
                -fx-text-fill: %s;
                -fx-accent: %s;
            }
            .button {
                -fx-background-color: %s;
                -fx-text-fill: %s;
                -fx-border-color: %s;
                -fx-border-width: 2px;
            }
            .button:hover {
                -fx-background-color: %s;
            }
            .text-field, .combo-box, .date-picker {
                -fx-background-color: %s;
                -fx-text-fill: %s;
                -fx-border-color: %s;
                -fx-border-width: 2px;
            }
            .table-view {
                -fx-background-color: %s;
                -fx-text-fill: %s;
                -fx-border-color: %s;
            }
            .table-cell {
                -fx-background-color: %s;
                -fx-text-fill: %s;
                -fx-border-color: %s;
            }
            """.formatted(
                colors.get("background").toString().replace("0x", "#"),
                colors.get("background").toString().replace("0x", "#"),
                colors.get("background").toString().replace("0x", "#"),
                colors.get("text").toString().replace("0x", "#"),
                colors.get("primary").toString().replace("0x", "#"),
                colors.get("primary").toString().replace("0x", "#"),
                colors.get("text").toString().replace("0x", "#"),
                colors.get("text").toString().replace("0x", "#"),
                colors.get("secondary").toString().replace("0x", "#"),
                colors.get("background").toString().replace("0x", "#"),
                colors.get("text").toString().replace("0x", "#"),
                colors.get("text").toString().replace("0x", "#"),
                colors.get("background").toString().replace("0x", "#"),
                colors.get("text").toString().replace("0x", "#"),
                colors.get("text").toString().replace("0x", "#"),
                colors.get("background").toString().replace("0x", "#"),
                colors.get("text").toString().replace("0x", "#"),
                colors.get("text").toString().replace("0x", "#")
            );
        
        scene.getStylesheets().add("data:text/css," + highContrastCSS);
    }
    
    /**
     * Apply large text mode to scene
     */
    private void applyLargeTextMode(Scene scene) {
        if (largeTextMode) {
            String largeTextCSS = """
                .root {
                    -fx-font-size: 16px;
                }
                .button {
                    -fx-font-size: 18px;
                    -fx-padding: 12px 24px;
                }
                .label {
                    -fx-font-size: 16px;
                }
                .text-field, .combo-box, .date-picker {
                    -fx-font-size: 16px;
                    -fx-padding: 8px;
                }
                .table-view {
                    -fx-font-size: 16px;
                }
                .menu-bar {
                    -fx-font-size: 18px;
                }
                """;
            scene.getStylesheets().add("data:text/css," + largeTextCSS);
        }
    }
    
    /**
     * Enable keyboard navigation
     */
    private void enableKeyboardNavigation(Scene scene) {
        scene.setOnKeyPressed(event -> {
            // Global keyboard shortcuts for accessibility
            switch (event.getCode().toString()) {
                case "F1" -> showHelpDialog();
                case "F2" -> toggleHighContrastMode();
                case "F3" -> toggleLargeTextMode();
                case "F4" -> toggleAccessibilityMode();
                case "ESCAPE" -> handleEscapeKey();
            }
        });
        
        logger.info("Keyboard navigation enabled");
    }
    
    /**
     * Show help dialog with accessibility shortcuts
     */
    public void showHelpDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Accessibility Help");
        alert.setHeaderText("Keyboard Shortcuts and Accessibility Features");
        
        String helpText = """
            Keyboard Shortcuts:
            • F1 - Show this help dialog
            • F2 - Toggle high contrast mode
            • F3 - Toggle large text mode
            • F4 - Toggle accessibility mode
            • Ctrl+Shift+A - Toggle accessibility mode
            • Alt+1-4 - Quick navigation
            • Escape - Close dialogs/cancel operations
            
            Accessibility Features:
            • High contrast mode for better visibility
            • Large text mode for easier reading
            • Screen reader compatibility
            • Keyboard navigation support
            • Voice announcements for important actions
            
            For PWD Users:
            • All functions accessible via keyboard
            • Clear visual indicators
            • Audio feedback for actions
            • Simplified navigation paths
            """;
        
        alert.setContentText(helpText);
        alert.getDialogPane().setPrefWidth(600);
        alert.showAndWait();
    }
    
    /**
     * Handle escape key press
     */
    private void handleEscapeKey() {
        // Close any open dialogs or cancel current operations
        logger.info("Escape key pressed - handling cancellation");
    }
    
    /**
     * Check system accessibility settings
     */
    private void checkSystemAccessibilitySettings() {
        // In a real implementation, this would check system accessibility settings
        // and automatically enable appropriate features
        try {
            // Check for high contrast mode
            String highContrast = System.getProperty("java.awt.highcontrast");
            if ("true".equalsIgnoreCase(highContrast)) {
                highContrastMode = true;
            }
            
            // Check for screen reader
            String screenReader = System.getProperty("java.awt.screenreader");
            if ("true".equalsIgnoreCase(screenReader)) {
                screenReaderEnabled = true;
            }
            
        } catch (Exception e) {
            logger.warning("Could not check system accessibility settings: " + e.getMessage());
        }
    }
    
    /**
     * Announce text for screen readers
     */
    public void announceText(String text) {
        if (screenReaderEnabled) {
            // In a real implementation, this would use Java Access Bridge
            // to announce text to screen readers
            logger.info("Screen reader announcement: " + text);
        }
    }
    
    /**
     * Get current accessibility status
     */
    public String getAccessibilityStatus() {
        return String.format(
            "Accessibility Status:\n" +
            "High Contrast: %s\n" +
            "Large Text: %s\n" +
            "Screen Reader: %s\n" +
            "Keyboard Navigation: %s\n" +
            "Language: %s",
            highContrastMode ? "Enabled" : "Disabled",
            largeTextMode ? "Enabled" : "Disabled",
            screenReaderEnabled ? "Enabled" : "Disabled",
            keyboardNavigationEnabled ? "Enabled" : "Disabled",
            currentLanguage
        );
    }
    
    // Getters and Setters
    public boolean isHighContrastMode() {
        return highContrastMode;
    }
    
    public void setHighContrastMode(boolean highContrastMode) {
        this.highContrastMode = highContrastMode;
    }
    
    public boolean isLargeTextMode() {
        return largeTextMode;
    }
    
    public void setLargeTextMode(boolean largeTextMode) {
        this.largeTextMode = largeTextMode;
    }
    
    public boolean isScreenReaderEnabled() {
        return screenReaderEnabled;
    }
    
    public void setScreenReaderEnabled(boolean screenReaderEnabled) {
        this.screenReaderEnabled = screenReaderEnabled;
    }
    
    public boolean isKeyboardNavigationEnabled() {
        return keyboardNavigationEnabled;
    }
    
    public void setKeyboardNavigationEnabled(boolean keyboardNavigationEnabled) {
        this.keyboardNavigationEnabled = keyboardNavigationEnabled;
    }
    
    public String getCurrentLanguage() {
        return currentLanguage;
    }
    
    public void setCurrentLanguage(String currentLanguage) {
        this.currentLanguage = currentLanguage;
    }
}
