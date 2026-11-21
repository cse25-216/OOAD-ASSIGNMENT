package controllers;

import app.Main;
import dao.AccountDAO;
import dao.TransactionDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Account;
import models.User;
import services.AccountService;
import utils.CurrencyUtil;

import java.util.ArrayList;
import java.util.List;

public class TransferController {

    @FXML private ComboBox<String> fromAccountSelect;
    @FXML private ComboBox<String> toAccountSelect;

    @FXML private Label fromBalanceLabel;
    @FXML private Label toBalanceLabel;
    @FXML private TextField amountField;

    private User user;
    private final List<Account> accounts = new ArrayList<>();
    private Account fromAccount;
    private Account toAccount;

    private final AccountDAO accountDAO = new AccountDAO();

    public void setUser(User user) {
        this.user = user;
        loadAccounts();
    }

    private void loadAccounts() {

        accounts.clear();
        fromAccountSelect.getItems().clear();
        toAccountSelect.getItems().clear();

        String[] types = {"Savings", "Investors", "Chequing"};

        for (String type : types) {
            Account account = accountDAO.getAccount(user.getId(), type);

            if (account != null) {
                accounts.add(account);
                fromAccountSelect.getItems().add(account.getType());
                toAccountSelect.getItems().add(account.getType());
            }
        }

        if (!accounts.isEmpty()) {
            fromAccountSelect.getSelectionModel().selectFirst();
            toAccountSelect.getSelectionModel().selectLast();
            updateFromAccount();
            updateToAccount();
        }
    }

    @FXML
    public void updateFromAccount() {

        String type = fromAccountSelect.getValue();

        if (type == null || user == null) {
            fromAccount = null;
            fromBalanceLabel.setText("Balance: P0.00");
            return;
        }

        fromAccount = accountDAO.getAccount(user.getId(), type);

        if (fromAccount != null) {
            fromBalanceLabel.setText(
                "Balance: " + CurrencyUtil.format(fromAccount.getBalance())
            );
        } else {
            fromBalanceLabel.setText("Balance: P0.00");
        }
    }

    @FXML
    public void updateToAccount() {

        String type = toAccountSelect.getValue();

        if (type == null || user == null) {
            toAccount = null;
            toBalanceLabel.setText("Balance: P0.00");
            return;
        }

        toAccount = accountDAO.getAccount(user.getId(), type);

        if (toAccount != null) {
            toBalanceLabel.setText(
                "Balance: " + CurrencyUtil.format(toAccount.getBalance())
            );
        } else {
            toBalanceLabel.setText("Balance: P0.00");
        }
    }

    @FXML
    private void handleTransfer() {

        String fromType = fromAccountSelect.getValue();
        String toType = toAccountSelect.getValue();

        if (fromType == null || toType == null) {
            showAlert("Error", "Select both accounts.");
            return;
        }

        if (fromType.equals(toType)) {
            showAlert("Invalid", "Cannot transfer to the same account.");
            return;
        }

        double amount;

        try {
            amount = Double.parseDouble(amountField.getText().trim());

            if (!Double.isFinite(amount) || amount <= 0) {
                showAlert("Invalid Amount",
                    "Enter an amount greater than zero.");
                return;
            }

        } catch (NumberFormatException e) {
            showAlert("Invalid Amount", "Enter a valid amount.");
            return;
        }

        fromAccount = accountDAO.getAccount(user.getId(), fromType);
        toAccount = accountDAO.getAccount(user.getId(), toType);

        if (fromAccount == null || toAccount == null) {
            showAlert("Error", "One or both accounts could not be found.");
            return;
        }

        if (fromAccount.getId() == toAccount.getId()) {
            showAlert("Invalid", "Cannot transfer to the same account.");
            return;
        }

        if (fromAccount.getBalance() < amount) {
            showAlert("Insufficient Funds",
                "Available balance: " +
                CurrencyUtil.format(fromAccount.getBalance()) +
                "\nTransfer amount: " +
                CurrencyUtil.format(amount));
            return;
        }

        boolean successful;

        try {
            successful = AccountService.transfer(
                fromAccount,
                toAccount,
                amount
            );
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Transfer Error",
                "An error occurred while processing the transfer.");
            return;
        }

        if (!successful) {
            showAlert("Transfer Failed",
                "The transfer could not be completed. " +
                "Please check the account service or database.");
            updateFromAccount();
            updateToAccount();
            return;
        }

        Account updatedFrom = accountDAO.getAccount(user.getId(), fromType);
        Account updatedTo = accountDAO.getAccount(user.getId(), toType);

        if (updatedFrom != null && updatedTo != null) {

            TransactionDAO.record(
                updatedFrom.getId(),
                "Transfer to " + toType,
                amount,
                updatedFrom.getBalance()
            );

            TransactionDAO.record(
                updatedTo.getId(),
                "Transfer from " + fromType,
                amount,
                updatedTo.getBalance()
            );
        }

        updateFromAccount();
        updateToAccount();

        amountField.clear();

        showAlert(
            "Success",
            "Transferred " + CurrencyUtil.format(amount) +
            " from " + fromType +
            " to " + toType + "."
        );
    }

    @FXML
    private void goBack() {
        Main.loadCustomerDashboard(user);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
