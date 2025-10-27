package com.cofitearia.milktea.controllers;

import com.cofitearia.milktea.models.Product;
import com.cofitearia.milktea.services.ProductService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MenuController implements Initializable {
    private static final Logger logger = Logger.getLogger(MenuController.class.getName());
    
    @FXML
    private GridPane productGrid;
    
    @FXML
    private Button bestSellerBtn;
    
    @FXML
    private Button mixedBtn;
    
    @FXML
    private Button milkteaBtn;
    
    @FXML
    private Button topPicksBtn;
    
    @FXML
    private Button coffeeBtn;
    
    @FXML
    private Button cartButton;
    
    @FXML
    private Button closeCartButton;
    
    @FXML
    private VBox cartItemsContainer;
    
    @FXML
    private ScrollPane cartScrollPane;
    
    @FXML
    private Label subtotalLabel;
    
    @FXML
    private Label taxLabel;
    
    @FXML
    private Label totalLabel;
    
    @FXML
    private Button checkoutButton;
    
    @FXML
    private Button clearCartButton;
    
    @FXML
    private VBox cartPanel;
    
    private ProductService productService;
    private Button currentSelectedCategory;
    private Map<Product, Integer> cart = new HashMap<>();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productService = new ProductService();
        currentSelectedCategory = bestSellerBtn;
        loadProducts("Best Seller");
        updateCartSummary();
    }
    
    @FXML
    private void toggleCart() {
        if (cartPanel != null) {
            boolean isVisible = cartPanel.isVisible();
            cartPanel.setVisible(!isVisible);
            cartPanel.setManaged(!isVisible);
            logger.info("Cart toggled: " + (!isVisible ? "visible" : "hidden"));
        }
    }
    
    @FXML
    private void showCategory(javafx.event.ActionEvent event) {
        Button clickedBtn = (Button) event.getSource();
        String category = clickedBtn.getText();
        
        resetCategoryButtons();
        
        clickedBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 5px; -fx-padding: 10px 15px; -fx-pref-width: 180px; -fx-alignment: center-left;");
        currentSelectedCategory = clickedBtn;
        
        loadProducts(category);
    }
    
    private void resetCategoryButtons() {
        String defaultStyle = "-fx-background-color: transparent; -fx-text-fill: #666666; -fx-font-size: 14px; -fx-background-radius: 5px; -fx-padding: 10px 15px; -fx-pref-width: 180px; -fx-alignment: center-left;";
        
        bestSellerBtn.setStyle(defaultStyle);
        mixedBtn.setStyle(defaultStyle);
        milkteaBtn.setStyle(defaultStyle);
        topPicksBtn.setStyle(defaultStyle);
        coffeeBtn.setStyle(defaultStyle);
    }
    
    private void loadProducts(String category) {
        try {
            productGrid.getChildren().clear();
            
            List<Product> products = productService.getProductsByCategory(category);
            
            if (products.isEmpty()) {
                products = getSampleProducts(category);
            }
            
            int col = 0;
            int row = 0;
            
            for (Product product : products) {
                VBox productCard = createProductCard(product);
                productGrid.add(productCard, col, row);
                
                col++;
                if (col >= 3) {
                    col = 0;
                    row++;
                }
            }
            
        } catch (Exception e) {
            logger.severe("Error loading products: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private VBox createProductCard(Product product) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 15px; -fx-padding: 15px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 5, 0, 2, 2);");
        card.setPrefWidth(200);
        card.setMaxWidth(200);
        
        javafx.scene.shape.Rectangle imagePlaceholder = new javafx.scene.shape.Rectangle(150, 150);
        imagePlaceholder.setFill(javafx.scene.paint.Paint.valueOf("#f0f0f0"));
        imagePlaceholder.setArcWidth(10);
        imagePlaceholder.setArcHeight(10);
        
        javafx.scene.layout.StackPane imageContainer = new javafx.scene.layout.StackPane(imagePlaceholder);
        
        try {
            String imagePath = "/images/products/" + product.getName().toLowerCase().replace(" ", "_") + ".png";
            java.io.InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream != null) {
                Image productImage = new Image(imageStream);
                ImageView imageView = new ImageView(productImage);
                imageView.setFitWidth(150);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                imageView.setCache(true);
                imageContainer.getChildren().clear();
                imageContainer.getChildren().add(imageView);
            } else {
                Label placeholderLabel = new Label("🥤");
                placeholderLabel.setStyle("-fx-font-size: 60px;");
                imageContainer.getChildren().add(placeholderLabel);
            }
        } catch (Exception e) {
            Label placeholderLabel = new Label("🥤");
            placeholderLabel.setStyle("-fx-font-size: 60px;");
            imageContainer.getChildren().clear();
            imageContainer.getChildren().addAll(imagePlaceholder, placeholderLabel);
        }
        
        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-wrap-text: true; -fx-text-alignment: center;");
        nameLabel.setMaxWidth(170);
        
        Label priceLabel = new Label(product.getFormattedPrice());
        priceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2196F3;");
        
        Button addToCartBtn = new Button("Add to Cart");
        addToCartBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 8px; -fx-padding: 8px 20px;");
        addToCartBtn.setPrefWidth(170);
        addToCartBtn.setOnAction(e -> addToCart(product));
        
        addToCartBtn.setOnMouseEntered(e -> addToCartBtn.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 8px; -fx-padding: 8px 20px;"));
        addToCartBtn.setOnMouseExited(e -> addToCartBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 8px; -fx-padding: 8px 20px;"));
        
        card.getChildren().addAll(imageContainer, nameLabel, priceLabel, addToCartBtn);
        
        return card;
    }
    
    private void addToCart(Product product) {
        cart.put(product, cart.getOrDefault(product, 0) + 1);
        updateCartDisplay();
        updateCartSummary();
        logger.info("Added " + product.getName() + " to cart");
    }
    
    private void updateCartDisplay() {
        cartItemsContainer.getChildren().clear();
        
        if (cart.isEmpty()) {
            Label emptyLabel = new Label("Your cart is empty");
            emptyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #999999;");
            cartItemsContainer.getChildren().add(emptyLabel);
            return;
        }
        
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            VBox cartItem = createCartItem(entry.getKey(), entry.getValue());
            cartItemsContainer.getChildren().add(cartItem);
        }
    }
    
    private VBox createCartItem(Product product, int quantity) {
        VBox item = new VBox(5);
        item.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #e0e0e0; -fx-border-radius: 5px; -fx-padding: 10px;");
        
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        Label priceLabel = new Label(product.getFormattedPrice());
        priceLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666666;");
        
        Button removeBtn = new Button("×");
        removeBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 15px; -fx-pref-width: 30px; -fx-pref-height: 30px;");
        removeBtn.setOnAction(e -> {
            if (quantity > 1) {
                cart.put(product, quantity - 1);
            } else {
                cart.remove(product);
            }
            updateCartDisplay();
            updateCartSummary();
        });
        
        header.getChildren().addAll(nameLabel, removeBtn);
        
        HBox footer = new HBox(10);
        footer.setAlignment(Pos.CENTER_LEFT);
        
        Label quantityLabel = new Label("Qty: " + quantity);
        quantityLabel.setStyle("-fx-font-size: 12px;");
        
        BigDecimal total = product.getPrice().multiply(new BigDecimal(quantity));
        Label totalLabel = new Label("₱" + String.format("%.2f", total));
        totalLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2196F3;");
        
        footer.getChildren().addAll(quantityLabel, totalLabel);
        
        item.getChildren().addAll(header, footer);
        
        return item;
    }
    
    private void updateCartSummary() {
        BigDecimal subtotal = BigDecimal.ZERO;
        
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            BigDecimal itemTotal = entry.getKey().getPrice().multiply(new BigDecimal(entry.getValue()));
            subtotal = subtotal.add(itemTotal);
        }
        
        BigDecimal tax = subtotal.multiply(new BigDecimal("0.12"));
        BigDecimal total = subtotal.add(tax);
        
        subtotalLabel.setText("₱" + String.format("%.2f", subtotal));
        taxLabel.setText("₱" + String.format("%.2f", tax));
        totalLabel.setText("₱" + String.format("%.2f", total));
    }
    
    @FXML
    private void handleCheckout() {
        if (cart.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Cart");
            alert.setHeaderText("Cart is Empty");
            alert.setContentText("Please add items to your cart before checkout.");
            alert.showAndWait();
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Checkout");
        alert.setHeaderText("Order Placed");
        alert.setContentText("Thank you for your order! Total: " + totalLabel.getText());
        alert.showAndWait();
        
        cart.clear();
        updateCartDisplay();
        updateCartSummary();
        logger.info("Checkout completed");
    }
    
    @FXML
    private void handleClearCart() {
        if (cart.isEmpty()) {
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Clear Cart");
        alert.setHeaderText("Clear Shopping Cart");
        alert.setContentText("Are you sure you want to clear all items from your cart?");
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == javafx.scene.control.ButtonType.OK) {
                cart.clear();
                updateCartDisplay();
                updateCartSummary();
                logger.info("Cart cleared");
            }
        });
    }
    
    private List<Product> getSampleProducts(String category) {
        java.util.List<Product> products = new java.util.ArrayList<>();
        
        Product p1 = new Product("Bubble Tea", "Delicious boba milk tea", new java.math.BigDecimal("120.00"), category);
        products.add(p1);
        
        Product p2 = new Product("Thai Iced Tea", "Authentic Thai tea", new java.math.BigDecimal("150.00"), category);
        products.add(p2);
        
        Product p3 = new Product("Brown Sugar Milk Tea", "Classic brown sugar flavor", new java.math.BigDecimal("140.00"), category);
        products.add(p3);
        
        Product p4 = new Product("Matcha Latte", "Premium matcha green tea", new java.math.BigDecimal("160.00"), category);
        products.add(p4);
        
        Product p5 = new Product("Taro Milktea", "Creamy taro flavor", new java.math.BigDecimal("130.00"), category);
        products.add(p5);
        
        return products;
    }
}
