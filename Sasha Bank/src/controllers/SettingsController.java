package controllers;

import app.Main;
import dao.AccountDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.User;

public class SettingsController {

    private User user;

    public void setUser(User user){
        this.user = user;
    }

    @FXML
    private void handleViewProfile() {
        Main.loadProfilePage(user);
    }

    @FXML
    private void handleCloseAccount() {
        Main.loadCloseAccountPage(user);
    }

    @FXML
    private void goBack() {
        Main.loadDashboard(user);
    }
}
