
package controllers;

import app.Main;
import dao.AccountDAO;
import dao.TransactionDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import models.Account;
import models.User;

public class PaymentsController {

    @FXML private ChoiceBox<String> billAccountBox;
    @FXML private ChoiceBox<String> billTypeBox;
    @FXML private TextField billAccountField;
    @FXML private TextField billAmountField;
    @FXML private Label paymentStatus;

    private User user;
    private final AccountDAO accountDAO = new AccountDAO();

    public void setUser(User user) {
        this.user = user;
        loadAccounts();
        loadBillTypes();
    }

    private void loadAccounts() {
        billAccountBox.getItems().clear();

        String[] types = {"Savings", "Chequing", "Investors"};

        for (String t : types) {
            Account acc = accountDAO.getAccount(user.getId(), t);
            if (acc != null) {
                billAccountBox.getItems().add(t);
            }
        }
    }

    private void loadBillTypes() {
        billTypeBox.getItems().addAll(
                "BTC Internet",
                "Mascom Airtime",
                "Orange Airtime",
                "Water Bill",
                "Electricity Bill"
        );
    }

    @FXML
    private void goBack() {
        Main.loadCustomerDashboard(user);
    }

    @FXML
    private void payBill() {

        paymentStatus.setText("");
        paymentStatus.setTextFill(Color.RED);

        String acctType = billAccountBox.getValue();
        String billType = billTypeBox.getValue();
        String reference = billAccountField.getText().trim();

        if (acctType == null || billType == null || reference.isEmpty()) {
            paymentStatus.setText("All fields are required.");
            return;
        }

        if ((billType.equals("Mascom Airtime") || billType.equals("Orange Airtime"))
                && !reference.matches("\\d{11}")) {

            paymentStatus.setText("Mascom/Orange require 11-digit number.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(billAmountField.getText());
            if (amount <= 0) throw new Exception();
        } catch (Exception e) {
            paymentStatus.setText("Invalid payment amount.");
            return;
        }

        Account acc = accountDAO.getAccount(user.getId(), acctType);

        if (acc.getBalance() < amount) {
            paymentStatus.setText("Insufficient funds.");
            return;
        }

        double newBalance = acc.getBalance() - amount;
        accountDAO.updateBalance(acc.getId(), newBalance);

        TransactionDAO.record(
                acc.getId(),
                billType + " - Ref: " + reference,
                amount,
                newBalance
        );

        paymentStatus.setTextFill(Color.GREEN);
        paymentStatus.setText("Payment successful!");

        billAmountField.clear();
        billAccountField.clear();
        billTypeBox.setValue(null);
    }
}
