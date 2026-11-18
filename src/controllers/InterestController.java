package controllers;

import app.Main;
import dao.AccountDAO;
import dao.TransactionDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import models.Account;
import models.User;

public class InterestController {

    @FXML private Label interestMessageLabel;
    @FXML private Button applyInterestButton;

    private User user;
    private final AccountDAO accountDAO = new AccountDAO();

    public void setUser(User user) {
        this.user = user;
        checkInterestOptions();
    }

    private void checkInterestOptions() {

        StringBuilder msg = new StringBuilder("Interest available for:\n");

        boolean hasSavings = accountDAO.getAccount(user.getId(), "Savings") != null;
        boolean hasInvestors = accountDAO.getAccount(user.getId(), "Investors") != null;

        if (!hasSavings && !hasInvestors) {
            interestMessageLabel.setText("No interest-bearing accounts found.");
            applyInterestButton.setDisable(true);
            return;
        }

        if (hasSavings) msg.append("• Savings (0.5%)\n");
        if (hasInvestors) msg.append("• Investors (1.2%)\n");

        interestMessageLabel.setText(msg.toString());
    }

    @FXML
    private void applyInterest() {

        StringBuilder appliedMsg = new StringBuilder("Interest applied:\n");

        Account savings = accountDAO.getAccount(user.getId(), "Savings");
        Account inv = accountDAO.getAccount(user.getId(), "Investors");

        
        if (savings != null) {
            double interest = savings.getBalance() * 0.005;
            double newBal = savings.getBalance() + interest;

            accountDAO.updateBalance(savings.getId(), newBal);

            TransactionDAO.record(savings.getId(),
                    "Interest (0.5%)",
                    interest,
                    newBal
            );

            appliedMsg.append("• Savings: +P").append(String.format("%.2f", interest)).append("\n");
        }

        
        if (inv != null) {
            double interest = inv.getBalance() * 0.012;
            double newBal = inv.getBalance() + interest;

            accountDAO.updateBalance(inv.getId(), newBal);

            TransactionDAO.record(inv.getId(),
                    "Interest (1.2%)",
                    interest,
                    newBal
            );

            appliedMsg.append("• Investors: +P").append(String.format("%.2f", interest)).append("\n");
        }

        showPopup("Success", appliedMsg.toString());
    }

    @FXML
    private void goBack() {
        Main.loadCustomerDashboard(user);
    }

    private void showPopup(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }
}
