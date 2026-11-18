package controllers;

import app.Main;
import dao.AccountDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import models.User;

public class CustomerDashboardController {

    @FXML private Label customerNameLabel;
    @FXML private Label customerUsernameLabel;
    @FXML private Label balanceLabel;
    @FXML private ImageView profileImage;

    private User user;
    private final AccountDAO accountDAO = new AccountDAO();

    public void setUser(User user) {
        this.user = user;

        customerNameLabel.setText(user.getFullName());
        customerUsernameLabel.setText("@" + user.getUsername());

        double balance = accountDAO.getUserBalance(user.getId());
        balanceLabel.setText(String.format("%.2f", balance));
    }

    @FXML private void handleWithdraw()   { Main.loadWithdraw(); }
    @FXML private void handleDeposit()    { Main.loadDeposit(); }
    @FXML private void handleTransactions() { Main.loadTransactions(); }
    @FXML private void handlePayment()    { Main.loadPayments(); }
    @FXML private void handleInterest()   { Main.loadInterest(); }
    @FXML private void handleTransfer()   { Main.loadTransfer(); }
    @FXML private void handleOpenAccount(){ Main.loadOpenAccount(); }

    @FXML private void logout() {
        Main.loadLogin();
    }
}
