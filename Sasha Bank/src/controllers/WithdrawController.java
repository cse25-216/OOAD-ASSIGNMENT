package controllers;

import app.Main;
import dao.AccountDAO;
import dao.TransactionDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Account;
import models.User;
import models.Withdrawable;
import services.AccountService;
import utils.CurrencyUtil;

import java.util.ArrayList;
import java.util.List;

public class WithdrawController {

    @FXML private ComboBox<String> accountSelect;
    @FXML private Label balanceLabel;
    @FXML private TextField amountField;

    private User user;
    private final AccountDAO accountDAO = new AccountDAO();
    private List<Account> accounts = new ArrayList<>();
    private Account selectedAccount;

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
            balanceLabel.setText("Balance: " + CurrencyUtil.format(selectedAccount.getBalance()));
        }
    }

    @FXML
    private void handleWithdraw() {

        if (selectedAccount == null) {
            showError("Select an account.");
            return;
        }

        if (!(selectedAccount instanceof Withdrawable)) {
            showError("This account cannot withdraw.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountField.getText());
        } catch (Exception e) {
            showError("Enter a valid amount.");
            return;
        }

        boolean ok = AccountService.withdraw(selectedAccount, amount);
        if (!ok) {
            showError("Insufficient balance.");
            return;
        }

        // Update DB
        accountDAO.updateBalance(selectedAccount.getId(), selectedAccount.getBalance());

        // Record WITH updated balance
        TransactionDAO.record(
                selectedAccount.getId(),
                "WITHDRAW",
                amount,
                selectedAccount.getBalance()
        );

        balanceLabel.setText("Balance: " + CurrencyUtil.format(selectedAccount.getBalance()));

        showSuccess("Withdrawal successful!");
    }

    @FXML
    private void goBack() {
        Main.loadCustomerDashboard(user);
    }

    private void showSuccess(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText("Success");
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText("Error");
        a.setContentText(msg);
        a.showAndWait();
    }
}
