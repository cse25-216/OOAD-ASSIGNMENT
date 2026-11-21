package controllers;

import app.Main;
import dao.AccountDAO;
import dao.TransactionDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Account;
import models.Transaction;
import models.User;
import utils.CurrencyUtil;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistoryController {

    @FXML private ComboBox<String> accountSelect;

    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, String> colAccountType;
    @FXML private TableColumn<Transaction, String> colType;
    @FXML private TableColumn<Transaction, String> colAmount;
    @FXML private TableColumn<Transaction, String> colBalance;
    @FXML private TableColumn<Transaction, String> colTimestamp;

    private User user;
    private final AccountDAO accountDAO = new AccountDAO();
    private final List<Account> accounts = new ArrayList<>();
    private Account selectedAccount;

    public void setUser(User user) {
        this.user = user;
        setupTable();
        loadAccounts();
    }

    private void setupTable() {

        colAccountType.setCellValueFactory(data ->
            new SimpleStringProperty(
                selectedAccount != null ? selectedAccount.getType() : ""
            )
        );

        colType.setCellValueFactory(data ->
            new SimpleStringProperty(
                data.getValue().getType()
            )
        );

        colAmount.setCellValueFactory(data ->
            new SimpleStringProperty(
                CurrencyUtil.format(data.getValue().getAmount())
            )
        );

        colBalance.setCellValueFactory(data ->
            new SimpleStringProperty(
                CurrencyUtil.format(data.getValue().getUpdatedBalance())
            )
        );

        colTimestamp.setCellValueFactory(data ->
            new SimpleStringProperty(
                data.getValue().getDate()
            )
        );
    }

    private void loadAccounts() {

        accounts.clear();
        accountSelect.getItems().clear();

        String[] types = {"Savings", "Investors", "Chequing"};

        for (String type : types) {

            Account account = accountDAO.getAccount(user.getId(), type);

            if (account != null) {
                accounts.add(account);
                accountSelect.getItems().add(account.getType());
            }
        }

        if (!accounts.isEmpty()) {
            accountSelect.getSelectionModel().selectFirst();
            loadTransactions();
        } else {
            selectedAccount = null;
            transactionTable.getItems().clear();
        }
    }

    @FXML
    public void loadTransactions() {

        String type = accountSelect.getValue();

        if (type == null) {
            selectedAccount = null;
            transactionTable.getItems().clear();
            return;
        }

        selectedAccount = accounts.stream()
                .filter(account -> account.getType().equals(type))
                .findFirst()
                .orElse(null);

        if (selectedAccount == null) {
            transactionTable.getItems().clear();
            return;
        }

        List<Transaction> transactions =
                TransactionDAO.getHistory(selectedAccount.getId());

        transactionTable.setItems(
                FXCollections.observableArrayList(transactions)
        );

        transactionTable.refresh();
    }

    @FXML
    private void goBack() {
        Main.loadCustomerDashboard(user);
    }
}
