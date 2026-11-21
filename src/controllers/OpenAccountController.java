package controllers;

import app.Main;
import dao.AccountDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.User;

public class OpenAccountController {

    @FXML private ComboBox<String> accountTypeSelect;

    private User user;
    private final AccountDAO accountDAO = new AccountDAO();

    @FXML
    public void initialize() {
    
        accountTypeSelect.getItems().addAll(
                "Savings",
                "Chequing",
                "Investors"
        );
    }

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    private void handleCreate() {

        String type = accountTypeSelect.getValue();

        if (type == null) {
            showAlert("Please select an account type.");
            return;
        }

        boolean success = accountDAO.createAccount(user.getId(), type);

        if (success) {
            showAlert("Account created successfully!");
        } else {
            showAlert("Failed to open account.");
        }
    }

    @FXML
    private void goBack() {
        Main.loadCustomerDashboard(user);
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Account Creation");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
