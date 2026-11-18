package controllers;

import app.Main;
import dao.UserDAO;
import dao.AccountDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.User;

public class AdminCreateCustomerController {

    @FXML private TextField fullNameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML private TextField birthPlaceField;
    @FXML private DatePicker dobPicker;
    @FXML private TextField idNumberField;

    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;

    @FXML private ComboBox<String> genderCombo;
    @FXML private ComboBox<String> accountTypeCombo;

    @FXML private TextField initialAmountField;

    private final UserDAO userDAO = new UserDAO();
    private final AccountDAO accountDAO = new AccountDAO();

    @FXML
    public void initialize() {
        genderCombo.getItems().addAll("Male", "Female", "Other");

        // NEW — selectable account type
        accountTypeCombo.getItems().addAll("Savings", "Chequing", "Investors");
    }

    @FXML
    public void createCustomer() {

        String fullName = fullNameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String birthPlace = birthPlaceField.getText();
        String dob = (dobPicker.getValue() != null) ? dobPicker.getValue().toString() : "";
        String nationalId = idNumberField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        String gender = genderCombo.getValue();
        String accountType = accountTypeCombo.getValue();
        String amountText = initialAmountField.getText();

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()
                || birthPlace.isEmpty() || dob.isEmpty() || nationalId.isEmpty()
                || phone.isEmpty() || email.isEmpty() || address.isEmpty()
                || gender == null || accountType == null || amountText.isEmpty()) {

            alert("All fields are required!");
            return;
        }

        double initialAmount;
        try {
            initialAmount = Double.parseDouble(amountText);
        } catch (Exception e) {
            alert("Initial amount must be a number.");
            return;
        }

        
        User user = new User(0, fullName, username, password, "CUSTOMER");
        int userId = userDAO.insertUserReturnID(user);

        if (userId <= 0) {
            alert("Failed to create user — username may already exist.");
            return;
        }

        
        accountDAO.createInitialAccount(userId, accountType, initialAmount);

        alert("Customer created!\nUser ID: " + userId);
        Main.loadAdminDashboard();
    }

    @FXML
    public void backToDashboard() {
        Main.loadAdminDashboard();
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
