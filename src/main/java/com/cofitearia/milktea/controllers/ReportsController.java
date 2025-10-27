package com.cofitearia.milktea.controllers;

import com.cofitearia.milktea.models.User;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Reports controller - placeholder for future implementation
 */
public class ReportsController implements Initializable {
    
    private User currentUser;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Placeholder implementation
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}
