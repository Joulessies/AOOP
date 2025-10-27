package com.cofitearia.milktea.controllers;

import com.cofitearia.milktea.Main;
import com.cofitearia.milktea.models.User;
import com.cofitearia.milktea.services.UserService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
 * Registration controller with accessibility features
 * Handles user registration with validation and PWD-friendly design
 */
public class RegisterController implements Initializable {
    private static final Logger logger = Logger.getLogger(RegisterController.class.getName());
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private PasswordField confirmPasswordField;
    
    @FXML
    private TextField firstNameField;
    
    @FXML
    private TextField lastNameField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private ComboBox<String> roleComboBox;
    
    @FXML
    private Button registerButton;
    
    @FXML
    private Hyperlink backToLoginLink;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private javafx.scene.layout.StackPane rootStackPane;
    
    private UserService userService;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userService = new UserService();
        setupAccessibility();
        setupRoleComboBox();
        setupKeyboardNavigation();
        setupEventHandlers();
        setupBackground();
        setupStyles();
        
        // Set focus to username field for better accessibility
        Platform.runLater(() -> usernameField.requestFocus());
        
        logger.info("Register controller initialized");
    }
    
    /**
     * Setup CSS styles
     */
    private void setupStyles() {
        // Apply styles to form fields
        usernameField.getStyleClass().add("form-field");
        passwordField.getStyleClass().add("form-field");
        confirmPasswordField.getStyleClass().add("form-field");
        firstNameField.getStyleClass().add("form-field");
        lastNameField.getStyleClass().add("form-field");
        emailField.getStyleClass().add("form-field");
        roleComboBox.getStyleClass().add("form-field");
        registerButton.getStyleClass().add("btn-primary");
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
        usernameField.setAccessibleText("Username input field. Enter your desired username here.");
        passwordField.setAccessibleText("Password input field. Enter your password here.");
        confirmPasswordField.setAccessibleText("Confirm password field. Re-enter your password to confirm.");
        firstNameField.setAccessibleText("First name input field. Enter your first name here.");
        lastNameField.setAccessibleText("Last name input field. Enter your last name here.");
        emailField.setAccessibleText("Email input field. Enter your email address here.");
        roleComboBox.setAccessibleText("Role selection. Choose your role in the system.");
        registerButton.setAccessibleText("Register button. Click to create your account.");
        backToLoginLink.setAccessibleText("Back to login link. Click to return to login screen.");
        
        // Set tooltips for additional help
        usernameField.setTooltip(new Tooltip("Choose a unique username"));
        passwordField.setTooltip(new Tooltip("Choose a strong password"));
        confirmPasswordField.setTooltip(new Tooltip("Re-enter your password"));
        firstNameField.setTooltip(new Tooltip("Enter your first name"));
        lastNameField.setTooltip(new Tooltip("Enter your last name"));
        emailField.setTooltip(new Tooltip("Enter your email address"));
        roleComboBox.setTooltip(new Tooltip("Select your role"));
        registerButton.setTooltip(new Tooltip("Click to register"));
        backToLoginLink.setTooltip(new Tooltip("Return to login screen"));
        
        // Apply accessibility styling if enabled
        if (Main.getAccessibilityManager().isHighContrastMode()) {
            applyHighContrastStyling();
        }
    }
    
    /**
     * Setup role combo box
     */
    private void setupRoleComboBox() {
        ObservableList<String> roles = FXCollections.observableArrayList(
            "STAFF",
            "PWD_STAFF",
            "MANAGER"
        );
        roleComboBox.setItems(roles);
        roleComboBox.setValue("STAFF"); // Default selection
    }
    
    /**
     * Setup keyboard navigation
     */
    private void setupKeyboardNavigation() {
        // Tab order setup
        usernameField.setFocusTraversable(true);
        passwordField.setFocusTraversable(true);
        confirmPasswordField.setFocusTraversable(true);
        firstNameField.setFocusTraversable(true);
        lastNameField.setFocusTraversable(true);
        emailField.setFocusTraversable(true);
        roleComboBox.setFocusTraversable(true);
        registerButton.setFocusTraversable(true);
        backToLoginLink.setFocusTraversable(true);
        
        // Keyboard event handlers
        usernameField.setOnKeyPressed(this::handleKeyPress);
        passwordField.setOnKeyPressed(this::handleKeyPress);
        confirmPasswordField.setOnKeyPressed(this::handleKeyPress);
        firstNameField.setOnKeyPressed(this::handleKeyPress);
        lastNameField.setOnKeyPressed(this::handleKeyPress);
        emailField.setOnKeyPressed(this::handleKeyPress);
        roleComboBox.setOnKeyPressed(this::handleKeyPress);
        registerButton.setOnKeyPressed(this::handleKeyPress);
    }
    
    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        // Register button click
        registerButton.setOnAction(event -> handleRegister());
        
        // Back to login link
        backToLoginLink.setOnAction(event -> navigateToLogin());
        
        // Enter key on confirm password field should trigger registration
        confirmPasswordField.setOnAction(event -> handleRegister());
    }
    
    /**
     * Handle registration attempt
     */
    @FXML
    private void handleRegister() {
        // Clear previous status messages
        statusLabel.setVisible(false);
        
        // Validate input fields
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String selectedRole = roleComboBox.getValue();
        
        // Validation
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || 
            firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            showStatusMessage("Please fill in all fields", Alert.AlertType.WARNING);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showStatusMessage("Passwords do not match", Alert.AlertType.ERROR);
            passwordField.clear();
            confirmPasswordField.clear();
            passwordField.requestFocus();
            return;
        }
        
        if (password.length() < 6) {
            showStatusMessage("Password must be at least 6 characters long", Alert.AlertType.WARNING);
            passwordField.clear();
            confirmPasswordField.clear();
            passwordField.requestFocus();
            return;
        }
        
        if (!isValidEmail(email)) {
            showStatusMessage("Please enter a valid email address", Alert.AlertType.WARNING);
            emailField.requestFocus();
            return;
        }
        
        try {
            // Check if username already exists
            if (userService.usernameExists(username)) {
                showStatusMessage("Username already exists. Please choose a different username.", Alert.AlertType.WARNING);
                usernameField.clear();
                usernameField.requestFocus();
                return;
            }
            
            // Create new user
            User.Role role = User.Role.valueOf(selectedRole);
            User newUser = new User(username, password, firstName, lastName, role);
            newUser.setEmail(email);
            
            // Set default accessibility preferences for PWD users
            if (role == User.Role.PWD_STAFF) {
                newUser.setHighContrastMode(true);
                newUser.setLargeTextMode(true);
                newUser.setScreenReaderEnabled(true);
                newUser.setKeyboardNavigationEnabled(true);
            }
            
            // Save user to database
            boolean success = userService.createUser(newUser);
            
            if (success) {
                // Announce successful registration for screen readers
                Main.getAccessibilityManager().announceText("Registration successful for " + newUser.getFullName());
                
                // Show success message and navigate to login
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Registration Successful");
                successAlert.setHeaderText("Account Created Successfully");
                successAlert.setContentText("Your account has been created successfully.\n\nUsername: " + username + "\nRole: " + role.getDisplayName() + 
                                          "\n\nYou can now login with your credentials.");
                successAlert.showAndWait();
                
                // Navigate back to login
                navigateToLogin();
            } else {
                showStatusMessage("Registration failed. Please try again.", Alert.AlertType.ERROR);
            }
            
        } catch (Exception e) {
            logger.severe("Registration error: " + e.getMessage());
            showStatusMessage("Registration failed: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Navigate back to login screen
     */
    private void navigateToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            scene.getStylesheets().add(getClass().getResource("/css/login.css").toExternalForm());
            
            // Apply accessibility settings to new scene
            Main.getAccessibilityManager().applyAccessibilitySettings(scene);
            
            Stage stage = Main.getPrimaryStage();
            stage.setScene(scene);
            stage.setTitle("Cofitearia Milktea - Login");
            stage.centerOnScreen();
            
            logger.info("Navigated back to login screen");
            
        } catch (IOException e) {
            logger.severe("Failed to load login screen: " + e.getMessage());
            showStatusMessage("Failed to load login screen. Please restart the application.", Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Handle keyboard press events
     */
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (event.getSource() == usernameField) {
                passwordField.requestFocus();
            } else if (event.getSource() == passwordField) {
                confirmPasswordField.requestFocus();
            } else if (event.getSource() == confirmPasswordField) {
                firstNameField.requestFocus();
            } else if (event.getSource() == firstNameField) {
                lastNameField.requestFocus();
            } else if (event.getSource() == lastNameField) {
                emailField.requestFocus();
            } else if (event.getSource() == emailField) {
                roleComboBox.requestFocus();
            } else if (event.getSource() == roleComboBox) {
                handleRegister();
            }
        } else if (event.getCode() == KeyCode.F1) {
            Main.getAccessibilityManager().showHelpDialog();
        } else if (event.getCode() == KeyCode.ESCAPE) {
            navigateToLogin();
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
            confirmPasswordField.setStyle(highContrastStyle);
            firstNameField.setStyle(highContrastStyle);
            lastNameField.setStyle(highContrastStyle);
            emailField.setStyle(highContrastStyle);
            roleComboBox.setStyle(highContrastStyle);
        }
    }
    
    /**
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
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
            alert.setTitle("Registration Error");
            alert.setHeaderText(message);
            alert.setContentText("Please check your input and try again.");
            alert.showAndWait();
        }
        
        // Clear status message after 5 seconds
        Platform.runLater(() -> {
            try {
                Thread.sleep(5000);
                Platform.runLater(() -> statusLabel.setVisible(false));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}
