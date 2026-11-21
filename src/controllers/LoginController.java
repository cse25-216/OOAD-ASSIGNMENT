package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.User;
import services.AuthService;
import app.Main;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    public void login() {

        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = AuthService.login(username, password);

        if (user == null) {
            errorLabel.setText("Invalid username or password.");
            return;
        }

        switch (user.getRole()) {

            case "ADMIN":
                Main.loadAdminDashboard();
                break;

            case "COMPANY":
                Main.loadCompanyDashboard(user);
                break;

            case "CUSTOMER":
                Main.loadCustomerDashboard(user);
                break;

            default:
                errorLabel.setText("Unknown user role: " + user.getRole());
        }
    }
}
