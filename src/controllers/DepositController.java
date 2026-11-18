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

public class DepositController {

    @FXML private ComboBox<String> accountSelect;
    @FXML private Label balanceLabel;
    @FXML private TextField amountField;

    private User user;
    private List<Account> accounts = new ArrayList<>();
    private Account selectedAccount;

    private final AccountDAO accountDAO = new AccountDAO();

    public void setUser(User user) {
        this.user = user;
        loadAccounts();
    }

    private void loadAccounts() {
        accounts.clear();
        accountSelect.getItems().clear();

        String[] types = {"Savings", "Investors", "Chequing"};

        for (String t : types) {
            Account acc = accountDAO.getAccount(user.getId(), t);
            if (acc != null) accounts.add(acc);
        }

        for (Account acc : accounts)
            accountSelect.getItems().add(acc.getType());

        if (!accounts.isEmpty()) {
            accountSelect.getSelectionModel().selectFirst();
            updateSelectedAccount();
        }
    }

    @FXML
    public void updateSelectedAccount() {
        String type = accountSelect.getValue();

        selectedAccount = accounts.stream()
                .filter(a -> a.getType().equals(type))
                .findFirst()
                .orElse(null);

        if (selectedAccount != null) {
            balanceLabel.setText("Balance: " +
                    CurrencyUtil.format(selectedAccount.getBalance()));
        }
    }

    @FXML
    private void handleDeposit() {

        if (selectedAccount == null) {
            showError("Please select an account.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountField.getText());
        } catch (NumberFormatException e) {
            showError("Enter a valid amount.");
            return;
        }

        if (amount <= 0) {
            showError("Amount must be greater than zero.");
            return;
        }

        boolean ok = AccountService.deposit(selectedAccount, amount);
        if (!ok) {
            showError("Deposit failed.");
            return;
        }

        
        accountDAO.updateBalance(selectedAccount.getId(), selectedAccount.getBalance());

        
        TransactionDAO.record(
                selectedAccount.getId(),
                "DEPOSIT",
                amount,
                selectedAccount.getBalance()
        );

        balanceLabel.setText("Balance: " +
                CurrencyUtil.format(selectedAccount.getBalance()));

        showSuccess("Deposit successful!");
    }

    @FXML
    private void goBack() {
        Main.loadCustomerDashboard(user);
    }

    private void showSuccess(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText("✔ Deposit Successful");
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText("⚠ Deposit Failed");
        a.setContentText(msg);
        a.showAndWait();
    }
}
