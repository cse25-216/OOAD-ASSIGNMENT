package controllers;

import app.Main;
import dao.CompanyDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Company;

public class AdminCreateCompanyController {

    @FXML private TextField companyNameField;
    @FXML private TextField businessIdField;
    @FXML private TextField regNumberField;
    @FXML private DatePicker regDatePicker;
    @FXML private TextField capitalField;

    @FXML
    private void handleCreateCompany() {

        String name = companyNameField.getText();
        String businessId = businessIdField.getText();
        String regNumber = regNumberField.getText();
        String regDate = regDatePicker.getValue() != null ? regDatePicker.getValue().toString() : null;

        double capital;

    
        if (name.isEmpty() || businessId.isEmpty() || regNumber.isEmpty() || regDate == null || capitalField.getText().isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        try {
            capital = Double.parseDouble(capitalField.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Starting capital must be a valid number.");
            return;
        }

        Company company = new Company(0, name, businessId, regNumber, regDate, capital);

        CompanyDAO.createCompany(company);

        showAlert("Success", "Company account created successfully!");

        Main.loadAdminDashboard();
    }

    @FXML
    private void goBack() {
        Main.loadAdminDashboard();
    }

    private void showAlert(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.show();
    }
}
