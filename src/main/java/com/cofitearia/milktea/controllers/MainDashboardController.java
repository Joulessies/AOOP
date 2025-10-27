package com.cofitearia.milktea.controllers;

import com.cofitearia.milktea.Main;
import com.cofitearia.milktea.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.Timer;

/**
 * Main dashboard controller with accessibility features
 * Provides navigation to all system modules with PWD-friendly design
 */
public class MainDashboardController implements Initializable {
    private static final Logger logger = Logger.getLogger(MainDashboardController.class.getName());
    
    @FXML
    private BorderPane mainBorderPane;
    
    @FXML
    private MenuBar menuBar;
    
    @FXML
    private ToolBar toolBar;
    
    @FXML
    private VBox navigationPanel;
    
    @FXML
    private Label welcomeLabel;
    
    @FXML
    private Label userInfoLabel;
    
    @FXML
    private Label timeLabel;
    
    @FXML
    private Button inventoryButton;
    
    @FXML
    private Button salesButton;
    
    @FXML
    private Button reportsButton;
    
    @FXML
    private Button usersButton;
    
    @FXML
    private Button settingsButton;
    
    @FXML
    private Button logoutButton;
    
    private User currentUser;
    private Timer clockTimer;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupAccessibility();
        setupNavigation();
        setupMenuBar();
        setupToolBar();
        startClock();
        
        logger.info("Main dashboard controller initialized");
    }
    
    /**
     * Set current user and update UI
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateUserInterface();
        
        // Apply user's accessibility preferences
        applyUserAccessibilityPreferences();
        
        logger.info("Current user set: " + user.getFullName() + " (" + user.getRole().getDisplayName() + ")");
    }
    
    /**
     * Setup accessibility features
     */
    private void setupAccessibility() {
        // Set accessibility properties for navigation buttons
        inventoryButton.setAccessibleText("Inventory Management - Click to manage products and stock levels");
        salesButton.setAccessibleText("Sales Processing - Click to process customer orders and sales");
        reportsButton.setAccessibleText("Reports and Analytics - Click to view sales and inventory reports");
        usersButton.setAccessibleText("User Management - Click to manage system users and permissions");
        settingsButton.setAccessibleText("System Settings - Click to configure system preferences");
        logoutButton.setAccessibleText("Logout - Click to sign out of the system");
        
        // Set tooltips for additional help
        inventoryButton.setTooltip(new Tooltip("Manage inventory and stock levels"));
        salesButton.setTooltip(new Tooltip("Process sales and customer orders"));
        reportsButton.setTooltip(new Tooltip("View reports and analytics"));
        usersButton.setTooltip(new Tooltip("Manage users and permissions"));
        settingsButton.setTooltip(new Tooltip("Configure system settings"));
        logoutButton.setTooltip(new Tooltip("Sign out of the system"));
        
        // Enable keyboard navigation
        enableKeyboardNavigation();
    }
    
    /**
     * Setup navigation panel
     */
    private void setupNavigation() {
        // Set button styles for better accessibility
        String buttonStyle = """
            -fx-background-color: #2196F3;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-font-size: 14px;
            -fx-padding: 12px 20px;
            -fx-background-radius: 5px;
            """;
        
        inventoryButton.setStyle(buttonStyle);
        salesButton.setStyle(buttonStyle);
        reportsButton.setStyle(buttonStyle);
        usersButton.setStyle(buttonStyle);
        settingsButton.setStyle(buttonStyle);
        
        // Logout button with different style
        String logoutStyle = """
            -fx-background-color: #F44336;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-font-size: 14px;
            -fx-padding: 12px 20px;
            -fx-background-radius: 5px;
            """;
        logoutButton.setStyle(logoutStyle);
        
        // Set button actions
        inventoryButton.setOnAction(e -> navigateToInventory());
        salesButton.setOnAction(e -> navigateToSales());
        reportsButton.setOnAction(e -> navigateToReports());
        usersButton.setOnAction(e -> navigateToUsers());
        settingsButton.setOnAction(e -> navigateToSettings());
        logoutButton.setOnAction(e -> handleLogout());
    }
    
    /**
     * Setup menu bar
     */
    private void setupMenuBar() {
        // File Menu
        Menu fileMenu = new Menu("_File");
        MenuItem newSaleItem = new MenuItem("New Sale");
        MenuItem printItem = new MenuItem("Print");
        MenuItem exitItem = new MenuItem("Exit");
        
        newSaleItem.setOnAction(e -> navigateToSales());
        exitItem.setOnAction(e -> handleLogout());
        
        fileMenu.getItems().addAll(newSaleItem, new SeparatorMenuItem(), printItem, new SeparatorMenuItem(), exitItem);
        
        // View Menu
        Menu viewMenu = new Menu("_View");
        MenuItem refreshItem = new MenuItem("Refresh");
        MenuItem accessibilityItem = new MenuItem("Accessibility Settings");
        
        refreshItem.setOnAction(e -> refreshDashboard());
        accessibilityItem.setOnAction(e -> showAccessibilitySettings());
        
        viewMenu.getItems().addAll(refreshItem, new SeparatorMenuItem(), accessibilityItem);
        
        // Help Menu
        Menu helpMenu = new Menu("_Help");
        MenuItem helpItem = new MenuItem("Help");
        MenuItem aboutItem = new MenuItem("About");
        
        helpItem.setOnAction(e -> showHelp());
        aboutItem.setOnAction(e -> showAbout());
        
        helpMenu.getItems().addAll(helpItem, new SeparatorMenuItem(), aboutItem);
        
        menuBar.getMenus().addAll(fileMenu, viewMenu, helpMenu);
        
        // Set accessibility properties
        menuBar.setAccessibleText("Main menu bar with File, View, and Help menus");
    }
    
    /**
     * Setup tool bar
     */
    private void setupToolBar() {
        Button quickSaleBtn = new Button("Quick Sale");
        Button inventoryBtn = new Button("Inventory");
        Button reportsBtn = new Button("Reports");
        
        quickSaleBtn.setOnAction(e -> navigateToSales());
        inventoryBtn.setOnAction(e -> navigateToInventory());
        reportsBtn.setOnAction(e -> navigateToReports());
        
        toolBar.getItems().addAll(quickSaleBtn, inventoryBtn, reportsBtn);
    }
    
    /**
     * Update user interface based on current user
     */
    private void updateUserInterface() {
        if (currentUser != null) {
            welcomeLabel.setText("Welcome, " + currentUser.getFirstName() + "!");
            userInfoLabel.setText(currentUser.getFullName() + " - " + currentUser.getRole().getDisplayName());
            
            // Enable/disable buttons based on user role
            usersButton.setDisable(!currentUser.hasPermission("MANAGE_USERS"));
            
            // Announce welcome message for screen readers
            Main.getAccessibilityManager().announceText("Welcome to the main dashboard, " + currentUser.getFullName());
        }
    }
    
    /**
     * Apply user's accessibility preferences
     */
    private void applyUserAccessibilityPreferences() {
        if (currentUser != null) {
            Main.getAccessibilityManager().setHighContrastMode(currentUser.isHighContrastMode());
            Main.getAccessibilityManager().setLargeTextMode(currentUser.isLargeTextMode());
            Main.getAccessibilityManager().setScreenReaderEnabled(currentUser.isScreenReaderEnabled());
            Main.getAccessibilityManager().setKeyboardNavigationEnabled(currentUser.isKeyboardNavigationEnabled());
            
            // Apply accessibility settings to current scene
            Scene scene = mainBorderPane.getScene();
            if (scene != null) {
                Main.getAccessibilityManager().applyAccessibilitySettings(scene);
            }
        }
    }
    
    /**
     * Enable keyboard navigation
     */
    private void enableKeyboardNavigation() {
        mainBorderPane.setOnKeyPressed(event -> {
            switch (event.getCode().toString()) {
                case "F1" -> showHelp();
                case "F2" -> navigateToInventory();
                case "F3" -> navigateToSales();
                case "F4" -> navigateToReports();
                case "F5" -> refreshDashboard();
                case "ESCAPE" -> handleLogout();
            }
        });
    }
    
    /**
     * Start clock timer
     */
    private void startClock() {
        clockTimer = new Timer();
        clockTimer.scheduleAtFixedRate(new java.util.TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> {
                    LocalDateTime now = LocalDateTime.now();
                    String timeString = now.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy - HH:mm:ss"));
                    timeLabel.setText(timeString);
                });
            }
        }, 0, 1000);
    }
    
    // Navigation Methods
    
    @FXML
    private void navigateToInventory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/inventory.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 800);
            
            Main.getAccessibilityManager().applyAccessibilitySettings(scene);
            InventoryController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            
            Stage stage = Main.getPrimaryStage();
            stage.setScene(scene);
            stage.setTitle("Inventory Management - Cofitearia Milktea");
            
            Main.getAccessibilityManager().announceText("Navigated to Inventory Management");
            
        } catch (IOException e) {
            logger.severe("Failed to load inventory screen: " + e.getMessage());
            showErrorDialog("Failed to load inventory management screen.");
        }
    }
    
    @FXML
    private void navigateToSales() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/sales.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 800);
            
            Main.getAccessibilityManager().applyAccessibilitySettings(scene);
            SalesController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            
            Stage stage = Main.getPrimaryStage();
            stage.setScene(scene);
            stage.setTitle("Sales Processing - Cofitearia Milktea");
            
            Main.getAccessibilityManager().announceText("Navigated to Sales Processing");
            
        } catch (IOException e) {
            logger.severe("Failed to load sales screen: " + e.getMessage());
            showErrorDialog("Failed to load sales processing screen.");
        }
    }
    
    @FXML
    private void navigateToReports() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/reports.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 800);
            
            Main.getAccessibilityManager().applyAccessibilitySettings(scene);
            ReportsController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            
            Stage stage = Main.getPrimaryStage();
            stage.setScene(scene);
            stage.setTitle("Reports and Analytics - Cofitearia Milktea");
            
            Main.getAccessibilityManager().announceText("Navigated to Reports and Analytics");
            
        } catch (IOException e) {
            logger.severe("Failed to load reports screen: " + e.getMessage());
            showErrorDialog("Failed to load reports screen.");
        }
    }
    
    @FXML
    private void navigateToUsers() {
        if (!currentUser.hasPermission("MANAGE_USERS")) {
            showErrorDialog("You do not have permission to access user management.");
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/users.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 800);
            
            Main.getAccessibilityManager().applyAccessibilitySettings(scene);
            UsersController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            
            Stage stage = Main.getPrimaryStage();
            stage.setScene(scene);
            stage.setTitle("User Management - Cofitearia Milktea");
            
            Main.getAccessibilityManager().announceText("Navigated to User Management");
            
        } catch (IOException e) {
            logger.severe("Failed to load users screen: " + e.getMessage());
            showErrorDialog("Failed to load user management screen.");
        }
    }
    
    @FXML
    private void navigateToSettings() {
        showAccessibilitySettings();
    }
    
    @FXML
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Confirm Logout");
        alert.setContentText("Are you sure you want to logout?");
        
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                Main.getAccessibilityManager().announceText("Logging out of the system");
                
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
                    Scene scene = new Scene(loader.load(), 800, 600);
                    
                    // Load CSS styles
                    scene.getStylesheets().add(getClass().getResource("/css/login.css").toExternalForm());
                    
                    Main.getAccessibilityManager().applyAccessibilitySettings(scene);
                    
                    Stage stage = Main.getPrimaryStage();
                    stage.setScene(scene);
                    stage.setTitle("Cofitearia Milktea - Login");
                    
                } catch (IOException e) {
                    logger.severe("Failed to return to login screen: " + e.getMessage());
                }
            }
        });
    }
    
    @FXML
    private void refreshDashboard() {
        updateUserInterface();
        Main.getAccessibilityManager().announceText("Dashboard refreshed");
    }
    
    @FXML
    private void showAccessibilitySettings() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Accessibility Settings");
        alert.setHeaderText("Current Accessibility Configuration");
        alert.setContentText(Main.getAccessibilityManager().getAccessibilityStatus());
        alert.showAndWait();
    }
    
    @FXML
    private void showHelp() {
        Main.getAccessibilityManager().showHelpDialog();
    }
    
    @FXML
    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Cofitearia Milktea Inventory & Sales Management System");
        alert.setContentText("""
            Version 1.0.0
            
            A comprehensive inventory and sales management system designed with accessibility in mind.
            
            Features:
            • Real-time inventory tracking
            • Sales processing and reporting
            • User management with role-based access
            • Accessibility features for PWD users
            • Automated reordering and alerts
            
            Built with JavaFX and Object-Oriented Programming principles.
            """);
        alert.showAndWait();
    }
    
    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Operation Failed");
        alert.setContentText(message);
        alert.showAndWait();
        
        Main.getAccessibilityManager().announceText("Error: " + message);
    }
}
