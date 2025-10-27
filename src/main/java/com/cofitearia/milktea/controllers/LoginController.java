package com.cofitearia.milktea.controllers;

import com.cofitearia.milktea.Main;
import com.cofitearia.milktea.models.User;
import com.cofitearia.milktea.services.UserService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Login controller with accessibility features
 * Supports keyboard navigation, screen reader compatibility, and PWD-friendly design
 */
public class LoginController implements Initializable {
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Button accessibilityButton;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private CheckBox rememberMeCheckBox;
    
    @FXML
    private Hyperlink forgotPasswordLink;
    
    @FXML
    private Hyperlink registerLink;
    
    @FXML
    private Button guestAccessButton;
    
    private UserService userService;
    private User currentUser;
    
    @FXML
    private javafx.scene.layout.StackPane rootStackPane;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userService = new UserService();
        setupAccessibility();
        setupKeyboardNavigation();
        setupEventHandlers();
        setupBackground();
        setupStyles();
        
        // Set focus to username field for better accessibility
        Platform.runLater(() -> usernameField.requestFocus());
        
        logger.info("Login controller initialized");
    }
    
    /**
     * Setup CSS styles
     */
    private void setupStyles() {
        // Apply styles to form fields
        usernameField.getStyleClass().add("form-field");
        passwordField.getStyleClass().add("form-field");
        loginButton.getStyleClass().add("btn-primary");
        guestAccessButton.getStyleClass().add("btn-guest");
        accessibilityButton.getStyleClass().add("btn-accessibility");
    }
    
    /**
     * Setup background pattern
     */
    private void setupBackground() {
        try {
            // Try to load PNG background
            java.io.InputStream pngStream = getClass().getResourceAsStream("/images/background.png");
            if (pngStream != null) {
                // Load as JavaFX Image
                javafx.scene.image.Image backgroundImage = new javafx.scene.image.Image(pngStream);
                
                if (backgroundImage != null && !backgroundImage.isError()) {
                    javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(backgroundImage);
                    // Make the background cover the entire window
                    imageView.fitWidthProperty().bind(rootStackPane.widthProperty());
                    imageView.fitHeightProperty().bind(rootStackPane.heightProperty());
                    imageView.setOpacity(0.6);
                    imageView.setPreserveRatio(true);
                    imageView.setSmooth(true);
                    imageView.setCache(true);
                    
                    // Insert background at the beginning of children
                    rootStackPane.getChildren().add(0, imageView);
                    logger.info("Background PNG image loaded successfully");
                    pngStream.close();
                    return;
                }
                pngStream.close();
            }
        } catch (Exception e) {
            logger.warning("Could not load PNG background: " + e.getMessage());
        }
        
        // Fallback: Apply subtle gradient background
        rootStackPane.setStyle("""
            -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #e8e8e8, #f0f0f0);
        """);
        logger.info("Background pattern applied (gradient fallback)");
    }
    
    /**
     * Setup accessibility features
     */
    private void setupAccessibility() {
        // Set accessibility properties
        usernameField.setAccessibleText("Username input field. Enter your username here.");
        passwordField.setAccessibleText("Password input field. Enter your password here.");
        loginButton.setAccessibleText("Login button. Click to sign in to the system.");
        guestAccessButton.setAccessibleText("Guest access button. Click to access dashboard without login.");
        accessibilityButton.setAccessibleText("Accessibility settings button. Click to configure accessibility options.");
        registerLink.setAccessibleText("Register link. Click to create a new account.");
        
        // Set tooltips for additional help
        usernameField.setTooltip(new Tooltip("Enter your username"));
        passwordField.setTooltip(new Tooltip("Enter your password"));
        loginButton.setTooltip(new Tooltip("Click to login"));
        accessibilityButton.setTooltip(new Tooltip("Configure accessibility settings"));
        
        // Apply accessibility styling if enabled
        if (Main.getAccessibilityManager().isHighContrastMode()) {
            applyHighContrastStyling();
        }
    }
    
    /**
     * Setup keyboard navigation
     */
    private void setupKeyboardNavigation() {
        // Tab order setup
        usernameField.setFocusTraversable(true);
        passwordField.setFocusTraversable(true);
        loginButton.setFocusTraversable(true);
        guestAccessButton.setFocusTraversable(true);
        accessibilityButton.setFocusTraversable(true);
        rememberMeCheckBox.setFocusTraversable(true);
        forgotPasswordLink.setFocusTraversable(true);
        registerLink.setFocusTraversable(true);
        
        // Keyboard event handlers
        usernameField.setOnKeyPressed(this::handleKeyPress);
        passwordField.setOnKeyPressed(this::handleKeyPress);
        loginButton.setOnKeyPressed(this::handleKeyPress);
        guestAccessButton.setOnKeyPressed(this::handleKeyPress);
        accessibilityButton.setOnKeyPressed(this::handleKeyPress);
    }
    
    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        // Login button click
        loginButton.setOnAction(event -> handleLogin());
        
        // Accessibility button click
        accessibilityButton.setOnAction(event -> showAccessibilitySettings());
        
        // Forgot password link
        forgotPasswordLink.setOnAction(event -> handleForgotPassword());
        
        // Register link
        registerLink.setOnAction(event -> navigateToRegister());
        
        // Guest access button
        guestAccessButton.setOnAction(event -> handleGuestAccess());
        
        // Enter key on password field should trigger login
        passwordField.setOnAction(event -> handleLogin());
    }
    
    /**
     * Handle login attempt
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showStatusMessage("Please enter both username and password", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            currentUser = userService.authenticateUser(username, password);
            
            if (currentUser != null && currentUser.isActive()) {
                // Update last login
                userService.updateLastLogin(currentUser);
                
                // Announce successful login for screen readers
                Main.getAccessibilityManager().announceText("Login successful. Welcome " + currentUser.getFullName());
                
                // Navigate to main application
                navigateToMainApplication();
            } else {
                showStatusMessage("Invalid username or password", Alert.AlertType.ERROR);
                // Clear password field for security
                passwordField.clear();
                usernameField.requestFocus();
            }
        } catch (Exception e) {
            logger.severe("Login error: " + e.getMessage());
            showStatusMessage("Login failed. Please try again.", Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Navigate to main application after successful login
     */
    private void navigateToMainApplication() {
        navigateToMainApplication(currentUser);
    }
    
    /**
     * Navigate to main application with specified user
     */
    private void navigateToMainApplication(User user) {
        try {
            // Create main dashboard with navigation bar
            javafx.scene.layout.BorderPane root = new javafx.scene.layout.BorderPane();
            root.setStyle("-fx-background-color: #f5f5f5;");
            
            // Load navigation bar
            FXMLLoader navLoader = new FXMLLoader(getClass().getResource("/fxml/navigation_bar.fxml"));
            javafx.scene.layout.HBox navigationBar = navLoader.load();
            NavigationController navController = navLoader.getController();
            navController.setCurrentUser(user);
            
            // Set navigation bar at top
            root.setTop(navigationBar);
            
            // Create main content area
            javafx.scene.layout.VBox mainContent = new javafx.scene.layout.VBox(20);
            mainContent.setAlignment(javafx.geometry.Pos.CENTER);
            mainContent.setStyle("-fx-padding: 50px;");
            
            // Welcome label
            Label welcomeLabel = new Label("Welcome, " + user.getFullName() + "!");
            welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2196F3;");
            
            // User info label
            Label userInfoLabel = new Label(user.getRole().getDisplayName() + (user.getUsername().equals("guest") ? " (Demo Mode)" : ""));
            userInfoLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666666;");
            
            // Dashboard title
            Label titleLabel = new Label("Cofitearia Milktea - Main Dashboard");
            titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333333;");
            
            // Quick stats cards
            javafx.scene.layout.HBox statsCards = createStatsCards();
            
            // Add content to main area
            mainContent.getChildren().addAll(welcomeLabel, userInfoLabel, titleLabel, statsCards);
            
            // Set main content in center
            root.setCenter(mainContent);
            
            Scene scene = new Scene(root, 1200, 800);
            Main.getAccessibilityManager().applyAccessibilitySettings(scene);
            
            Stage stage = Main.getPrimaryStage();
            stage.setScene(scene);
            stage.setTitle("Cofitearia Milktea - Main Dashboard");
            stage.centerOnScreen();
            
            logger.info("Navigated to main dashboard for user: " + user.getUsername());
            
        } catch (Exception e) {
            logger.severe("Error creating main dashboard: " + e.getMessage());
            e.printStackTrace();
            showStatusMessage("Error creating dashboard: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Show accessibility settings dialog
     */
    @FXML
    private void showAccessibilitySettings() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Accessibility Settings");
        alert.setHeaderText("Configure Accessibility Options");
        
        String accessibilityInfo = Main.getAccessibilityManager().getAccessibilityStatus();
        alert.setContentText(accessibilityInfo + "\n\nWould you like to toggle accessibility mode?");
        
        alert.getButtonTypes().setAll(
            new ButtonType("Enable Accessibility", ButtonBar.ButtonData.YES),
            new ButtonType("Disable Accessibility", ButtonBar.ButtonData.NO),
            new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE)
        );
        
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType.getButtonData() == ButtonBar.ButtonData.YES) {
                Main.getAccessibilityManager().toggleAccessibilityMode();
                applyHighContrastStyling();
                showStatusMessage("Accessibility mode enabled", Alert.AlertType.INFORMATION);
            } else if (buttonType.getButtonData() == ButtonBar.ButtonData.NO) {
                Main.getAccessibilityManager().setHighContrastMode(false);
                Main.getAccessibilityManager().setLargeTextMode(false);
                showStatusMessage("Accessibility mode disabled", Alert.AlertType.INFORMATION);
            }
        });
    }
    
    /**
     * Create stats cards for the dashboard
     */
    private javafx.scene.layout.HBox createStatsCards() {
        javafx.scene.layout.HBox statsCards = new javafx.scene.layout.HBox(20);
        statsCards.setAlignment(javafx.geometry.Pos.CENTER);
        
        // Inventory card
        javafx.scene.layout.VBox inventoryCard = new javafx.scene.layout.VBox(10);
        inventoryCard.setAlignment(javafx.geometry.Pos.CENTER);
        inventoryCard.setStyle("-fx-background-color: #e3f2fd; -fx-background-radius: 10; -fx-border-color: #2196F3; -fx-border-radius: 10; -fx-padding: 20px;");
        
        Label inventoryIcon = new Label("📦");
        inventoryIcon.setStyle("-fx-font-size: 24px;");
        Label inventoryTitle = new Label("Inventory");
        inventoryTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #2196F3;");
        Label inventoryDesc = new Label("Manage stock levels");
        inventoryDesc.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");
        
        inventoryCard.getChildren().addAll(inventoryIcon, inventoryTitle, inventoryDesc);
        
        // Sales card
        javafx.scene.layout.VBox salesCard = new javafx.scene.layout.VBox(10);
        salesCard.setAlignment(javafx.geometry.Pos.CENTER);
        salesCard.setStyle("-fx-background-color: #e8f5e8; -fx-background-radius: 10; -fx-border-color: #4CAF50; -fx-border-radius: 10; -fx-padding: 20px;");
        
        Label salesIcon = new Label("💰");
        salesIcon.setStyle("-fx-font-size: 24px;");
        Label salesTitle = new Label("Sales");
        salesTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #4CAF50;");
        Label salesDesc = new Label("Process orders");
        salesDesc.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");
        
        salesCard.getChildren().addAll(salesIcon, salesTitle, salesDesc);
        
        // Reports card
        javafx.scene.layout.VBox reportsCard = new javafx.scene.layout.VBox(10);
        reportsCard.setAlignment(javafx.geometry.Pos.CENTER);
        reportsCard.setStyle("-fx-background-color: #fff3e0; -fx-background-radius: 10; -fx-border-color: #FF9800; -fx-border-radius: 10; -fx-padding: 20px;");
        
        Label reportsIcon = new Label("📊");
        reportsIcon.setStyle("-fx-font-size: 24px;");
        Label reportsTitle = new Label("Reports");
        reportsTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #FF9800;");
        Label reportsDesc = new Label("View analytics");
        reportsDesc.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");
        
        reportsCard.getChildren().addAll(reportsIcon, reportsTitle, reportsDesc);
        
        statsCards.getChildren().addAll(inventoryCard, salesCard, reportsCard);
        
        return statsCards;
    }
    
    /**
     * Handle guest access (demo mode)
     */
    @FXML
    private void handleGuestAccess() {
        try {
            // Create a guest user for demo purposes
            User guestUser = new User("guest", "", "Demo", "User", User.Role.STAFF);
            guestUser.setEmail("demo@cofitearia.com");
            
            // Announce guest access for screen readers
            Main.getAccessibilityManager().announceText("Entering guest demo mode");
            
            // Navigate directly to dashboard
            navigateToMainApplication(guestUser);
            
            logger.info("Guest access granted - demo mode activated");
            
        } catch (Exception e) {
            logger.severe("Error in guest access: " + e.getMessage());
            showStatusMessage("Failed to access demo mode. Please try again.", Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Navigate to registration screen
     */
    @FXML
    private void navigateToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
            Scene scene = new Scene(loader.load(), 900, 700);
            scene.getStylesheets().add(getClass().getResource("/css/login.css").toExternalForm());
            
            // Apply accessibility settings to new scene
            Main.getAccessibilityManager().applyAccessibilitySettings(scene);
            
            Stage stage = Main.getPrimaryStage();
            stage.setScene(scene);
            stage.setTitle("Cofitearia Milktea - User Registration");
            stage.centerOnScreen();
            
            logger.info("Navigated to registration screen");
            
        } catch (IOException e) {
            logger.severe("Failed to load registration screen: " + e.getMessage());
            showStatusMessage("Failed to load registration screen.", Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Handle forgot password
     */
    private void handleForgotPassword() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Forgot Password");
        alert.setHeaderText("Password Recovery");
        alert.setContentText("Please contact your system administrator to reset your password.\n\n" +
                           "For PWD users, assistance is available by calling the accessibility hotline.");
        alert.showAndWait();
    }
    
    /**
     * Handle keyboard press events
     */
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (event.getSource() == usernameField) {
                passwordField.requestFocus();
            } else if (event.getSource() == passwordField) {
                handleLogin();
            }
        } else if (event.getCode() == KeyCode.F1) {
            Main.getAccessibilityManager().showHelpDialog();
        } else if (event.isControlDown() && event.getCode() == KeyCode.A) {
            showAccessibilitySettings();
        }
    }
    
    /**
     * Apply high contrast styling
     */
    private void applyHighContrastStyling() {
        if (Main.getAccessibilityManager().isHighContrastMode()) {
            // Apply high contrast styles to form elements
            String highContrastStyle = """
                -fx-background-color: #000000;
                -fx-text-fill: #FFFFFF;
                -fx-border-color: #FFFFFF;
                -fx-border-width: 2px;
                """;
            
            usernameField.setStyle(highContrastStyle);
            passwordField.setStyle(highContrastStyle);
            loginButton.setStyle(highContrastStyle);
        }
    }
    
    /**
     * Show status message with accessibility support
     */
    private void showStatusMessage(String message, Alert.AlertType alertType) {
        statusLabel.setText(message);
        statusLabel.setVisible(true);
        
        // Announce message for screen readers
        Main.getAccessibilityManager().announceText(message);
        
        // Show alert dialog for important messages
        if (alertType == Alert.AlertType.ERROR) {
            Alert alert = new Alert(alertType);
            alert.setTitle("Login Error");
            alert.setHeaderText(message);
            alert.setContentText("Please check your credentials and try again.");
            alert.showAndWait();
        }
        
        // Clear status message after 3 seconds
        Platform.runLater(() -> {
            try {
                Thread.sleep(3000);
                Platform.runLater(() -> statusLabel.setVisible(false));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}
