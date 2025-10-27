package com.cofitearia.milktea.controllers;

import com.cofitearia.milktea.Main;
import com.cofitearia.milktea.models.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Navigation bar controller for the main application
 * Handles navigation actions and user interface updates
 */
public class NavigationController implements Initializable {
    private static final Logger logger = Logger.getLogger(NavigationController.class.getName());
    
    @FXML
    private Circle logoCircle;
    
    @FXML
    private javafx.scene.image.ImageView logoImageView;
    
    @FXML
    private Button orderNowButton;
    
    @FXML
    private Hyperlink locationLink;
    
    @FXML
    private Hyperlink aboutLink;
    
    @FXML
    private Hyperlink menuLink;
    
    @FXML
    private Hyperlink rateLink;
    
    @FXML
    private VBox userInfoPanel;
    
    @FXML
    private Label userNameLabel;
    
    @FXML
    private Hyperlink logoutLink;
    
    private User currentUser;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupAccessibility();
        setupEventHandlers();
        logger.info("Navigation controller initialized");
    }
    
    /**
     * Setup accessibility features
     */
    private void setupAccessibility() {
        // Set accessibility properties
        orderNowButton.setAccessibleText("Order Now button. Click to start a new order.");
        locationLink.setAccessibleText("Location link. Click to view shop location.");
        aboutLink.setAccessibleText("About Us link. Click to learn about Cofitearia Milktea.");
        menuLink.setAccessibleText("Menu link. Click to view our menu.");
        rateLink.setAccessibleText("Rate Us link. Click to rate our service.");
        logoutLink.setAccessibleText("Logout link. Click to logout from the system.");
        
        // Set tooltips
        orderNowButton.setTooltip(new Tooltip("Start a new order"));
        locationLink.setTooltip(new Tooltip("View shop location"));
        aboutLink.setTooltip(new Tooltip("Learn about Cofitearia Milktea"));
        menuLink.setTooltip(new Tooltip("View our menu"));
        rateLink.setTooltip(new Tooltip("Rate our service"));
        logoutLink.setTooltip(new Tooltip("Logout from the system"));
    }
    
    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        orderNowButton.setOnAction(event -> handleOrderNow());
        locationLink.setOnAction(event -> handleLocation());
        aboutLink.setOnAction(event -> handleAbout());
        menuLink.setOnAction(event -> handleMenu());
        rateLink.setOnAction(event -> handleRateUs());
        logoutLink.setOnAction(event -> handleLogout());
    }
    
    /**
     * Set current user and update UI
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateUserInterface();
    }
    
    /**
     * Update user interface based on current user
     */
    private void updateUserInterface() {
        if (currentUser != null) {
            userNameLabel.setText(currentUser.getFullName());
            userInfoPanel.setVisible(true);
            
            // Show different text for guest users
            if (currentUser.getUsername().equals("guest")) {
                userNameLabel.setText("Demo User");
            }
            
            logger.info("Navigation UI updated for user: " + currentUser.getUsername());
        } else {
            userInfoPanel.setVisible(false);
        }
    }
    
    /**
     * Handle Order Now button click
     */
    @FXML
    private void handleOrderNow() {
        Main.getAccessibilityManager().announceText("Starting new order");
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Now");
        alert.setHeaderText("New Order");
        alert.setContentText("Starting a new order...\n\nThis feature will be implemented in the sales module.");
        alert.showAndWait();
        
        logger.info("Order Now clicked by user: " + (currentUser != null ? currentUser.getUsername() : "anonymous"));
    }
    
    /**
     * Handle Location link click
     */
    @FXML
    private void handleLocation() {
        Main.getAccessibilityManager().announceText("Showing location information");
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Location");
        alert.setHeaderText("Cofitearia Milktea Location");
        alert.setContentText("""
            📍 Address: 123 Main Street, Downtown District
            🕒 Hours: Monday - Sunday: 8:00 AM - 10:00 PM
            📞 Phone: (555) 123-4567
            📧 Email: info@cofitearia.com
            
            Come visit us for the freshest milk tea in town!
            """);
        alert.showAndWait();
        
        logger.info("Location clicked by user: " + (currentUser != null ? currentUser.getUsername() : "anonymous"));
    }
    
    /**
     * Handle About Us link click
     */
    @FXML
    private void handleAbout() {
        Main.getAccessibilityManager().announceText("Showing about us information");
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Us");
        alert.setHeaderText("About Cofitearia Milktea");
        alert.setContentText("""
            🧋 Cofitearia Milktea - Brewing Fresh Daily
            
            We are passionate about creating the perfect milk tea experience 
            for our customers. Our commitment to quality and accessibility 
            makes us the preferred choice for milk tea lovers.
            
            ✨ Features:
            • Fresh ingredients daily
            • Accessible design for PWD customers
            • Friendly and inclusive environment
            • Wide variety of flavors
            • Customizable options
            
            Thank you for choosing Cofitearia Milktea!
            """);
        alert.showAndWait();
        
        logger.info("About Us clicked by user: " + (currentUser != null ? currentUser.getUsername() : "anonymous"));
    }
    
    /**
     * Handle Menu link click
     */
    @FXML
    private void handleMenu() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/menu.fxml"));
            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load(), 1200, 800);
            
            Main.getAccessibilityManager().applyAccessibilitySettings(scene);
            
            Main.getPrimaryStage().setScene(scene);
            Main.getPrimaryStage().setTitle("Menu - Cofitearia Milktea");
            
            Main.getAccessibilityManager().announceText("Navigated to menu");
            
            logger.info("Menu opened by user: " + (currentUser != null ? currentUser.getUsername() : "anonymous"));
            
        } catch (Exception e) {
            logger.severe("Failed to load menu: " + e.getMessage());
            e.printStackTrace();
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load menu");
            alert.setContentText("Could not load the menu. Please try again.");
            alert.showAndWait();
        }
    }
    
    /**
     * Handle Rate Us link click
     */
    @FXML
    private void handleRateUs() {
        Main.getAccessibilityManager().announceText("Opening rating dialog");
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Rate Us");
        alert.setHeaderText("How was your experience?");
        alert.setContentText("Please rate your experience at Cofitearia Milktea:");
        
        ButtonType excellentButton = new ButtonType("⭐⭐⭐⭐⭐ Excellent");
        ButtonType goodButton = new ButtonType("⭐⭐⭐⭐ Good");
        ButtonType averageButton = new ButtonType("⭐⭐⭐ Average");
        ButtonType poorButton = new ButtonType("⭐⭐ Poor");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes().setAll(excellentButton, goodButton, averageButton, poorButton, cancelButton);
        
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == excellentButton) {
                showThankYouMessage("Excellent! Thank you for the 5-star rating!");
            } else if (buttonType == goodButton) {
                showThankYouMessage("Good! Thank you for the 4-star rating!");
            } else if (buttonType == averageButton) {
                showThankYouMessage("Thank you for the 3-star rating. We'll work to improve!");
            } else if (buttonType == poorButton) {
                showThankYouMessage("We apologize for the poor experience. We'll do better!");
            }
        });
        
        logger.info("Rate Us clicked by user: " + (currentUser != null ? currentUser.getUsername() : "anonymous"));
    }
    
    /**
     * Show thank you message
     */
    private void showThankYouMessage(String message) {
        Alert thankYouAlert = new Alert(Alert.AlertType.INFORMATION);
        thankYouAlert.setTitle("Thank You");
        thankYouAlert.setHeaderText("Feedback Received");
        thankYouAlert.setContentText(message);
        thankYouAlert.showAndWait();
        
        Main.getAccessibilityManager().announceText(message);
    }
    
    /**
     * Handle Logout link click
     */
    @FXML
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Confirm Logout");
        alert.setContentText("Are you sure you want to logout?");
        
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                Main.getAccessibilityManager().announceText("Logging out of the system");
                
                // Navigate back to login screen
                try {
                    javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/login.fxml"));
                    javafx.scene.Scene scene = new javafx.scene.Scene(loader.load(), 800, 600);
                    
                    // Load CSS styles
                    scene.getStylesheets().add(getClass().getResource("/css/login.css").toExternalForm());
                    
                    Main.getAccessibilityManager().applyAccessibilitySettings(scene);
                    
                    Main.getPrimaryStage().setScene(scene);
                    Main.getPrimaryStage().setTitle("Cofitearia Milktea - Login");
                    
                    logger.info("User logged out: " + (currentUser != null ? currentUser.getUsername() : "unknown"));
                } catch (Exception e) {
                    logger.severe("Error during logout: " + e.getMessage());
                }
            }
        });
    }
}
