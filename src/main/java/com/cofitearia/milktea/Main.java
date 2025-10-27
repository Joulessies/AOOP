package com.cofitearia.milktea;

import com.cofitearia.milktea.database.DatabaseManager;
import com.cofitearia.milktea.utils.AccessibilityManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main application class for Cofitearia Milktea Inventory and Sales Management System
 * 
 * This system provides:
 * - Inventory management with real-time tracking
 * - Sales processing and reporting
 * - User management with role-based access
 * - Accessibility features for PWD users
 * - Automated reordering and alerts
 */
public class Main extends Application {
    
    private static Stage primaryStage;
    private static DatabaseManager databaseManager;
    private static AccessibilityManager accessibilityManager;
    
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        
        // Initialize database
        databaseManager = new DatabaseManager();
        databaseManager.initializeDatabase();
        
        // Initialize accessibility manager
        accessibilityManager = new AccessibilityManager();
        accessibilityManager.initializeAccessibility();
        
        // Load login screen
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        
        // Load CSS styles
        scene.getStylesheets().add(getClass().getResource("/css/login.css").toExternalForm());
        
        // Apply accessibility settings
        accessibilityManager.applyAccessibilitySettings(scene);
        
        stage.setTitle("Cofitearia Milktea - Inventory & Sales Management");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        
        // Set application icon
        try {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
        } catch (Exception e) {
            System.out.println("Icon not found, using default");
        }
        
        stage.show();
        
        // Set up global keyboard shortcuts for accessibility
        setupGlobalKeyboardShortcuts(scene);
    }
    
    private void setupGlobalKeyboardShortcuts(Scene scene) {
        scene.setOnKeyPressed(event -> {
            // F1 - Help
            if (event.getCode().toString().equals("F1")) {
                accessibilityManager.showHelpDialog();
            }
            // Ctrl+Shift+A - Toggle accessibility mode
            else if (event.isControlDown() && event.isShiftDown() && 
                     event.getCode().toString().equals("A")) {
                accessibilityManager.toggleAccessibilityMode();
            }
            // Alt+1-4 - Quick navigation
            else if (event.isAltDown()) {
                switch (event.getCode().toString()) {
                    case "DIGIT1" -> navigateToInventory();
                    case "DIGIT2" -> navigateToSales();
                    case "DIGIT3" -> navigateToReports();
                    case "DIGIT4" -> navigateToSettings();
                }
            }
        });
    }
    
    private void navigateToInventory() {
        // Implementation will be added when navigation is set up
        System.out.println("Navigating to Inventory");
    }
    
    private void navigateToSales() {
        System.out.println("Navigating to Sales");
    }
    
    private void navigateToReports() {
        System.out.println("Navigating to Reports");
    }
    
    private void navigateToSettings() {
        System.out.println("Navigating to Settings");
    }
    
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    
    public static AccessibilityManager getAccessibilityManager() {
        return accessibilityManager;
    }
    
    @Override
    public void stop() {
        if (databaseManager != null) {
            databaseManager.closeConnection();
        }
    }
    
    public static void main(String[] args) {
        launch();
    }
}
