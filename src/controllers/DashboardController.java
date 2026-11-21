package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Account;
import models.User;
import dao.AccountDAO;

public class DashboardController {

    @FXML private Label balanceLabel;

    private User user;
    private Account account;

    public void setUser(User user) {
        this.user = user;
        this.account = AccountDAO.getAccount(user.getId(), "Savings");  
        balanceLabel.setText(String.format("%.2f", account.getBalance()));
    }
}
