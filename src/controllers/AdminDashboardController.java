package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import app.Main;

public class AdminDashboardController {

    @FXML private Button createCustomerBtn;
    @FXML private Button createCompanyBtn;
    @FXML private Button logoutBtn;

    @FXML
    public void initialize() {
        System.out.println("Admin dashboard loaded successfully!");
    }

    @FXML
    private void openCreateCustomer() {
        Main.loadAdminCreateCustomer();
    }

    @FXML
    private void openCreateCompany() {
        Main.loadAdminCreateCompany();
    }

    @FXML
    private void logout() {
        Main.loadLogin();
    }
}
