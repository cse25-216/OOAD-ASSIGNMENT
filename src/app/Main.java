package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.User;

public class Main extends Application {

    private static Stage window;
    private static User loggedUser;

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        try (java.sql.Connection connection = dao.Database.getConnection()) {
            if (connection == null) throw new IllegalStateException("Could not initialize Sasha Bank database");
        } catch (java.sql.SQLException e) {
            throw new IllegalStateException("Could not initialize Sasha Bank database", e);
        }
        loadLogin();
        window.setTitle("Sasha Bank");
        window.show();
    }

    private static void loadView(String path, int w, int h) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(path));
            Scene scene = new Scene(loader.load(), w, h);
            window.setScene(scene);
        } catch (Exception e) {
            System.out.println("Error loading: " + path);
            e.printStackTrace();
        }
    }

    public static void loadLogin() {
        loadView("/views/login.fxml", 700, 500);
    }

    public static void loadCustomerDashboard(User user) {
        loggedUser = user;
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/views/customer_dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 1100, 700);
            controllers.CustomerDashboardController controller = loader.getController();
            controller.setUser(user);
            window.setScene(scene);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void loadCompanyDashboard(User user) {
        loggedUser = user;
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/views/company_dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 1100, 700);
            controllers.CompanyDashboardController controller = loader.getController();
            controller.setUser(user);
            window.setScene(scene);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void loadAdminDashboard() {
        loadView("/views/admin_dashboard.fxml", 1100, 700);
    }

    public static void loadAdminCreateCustomer() {
        loadView("/views/admin_create_customer.fxml", 900, 600);
    }

    public static void loadAdminCreateCompany() {
        loadView("/views/admin_create_company.fxml", 900, 600);
    }

    public static void loadWithdraw() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/views/withdraw.fxml"));
            Scene scene = new Scene(loader.load(), 700, 500);
            controllers.WithdrawController controller = loader.getController();
            controller.setUser(loggedUser);
            window.setScene(scene);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void loadDeposit() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/views/deposit.fxml"));
            Scene scene = new Scene(loader.load(), 700, 500);
            controllers.DepositController controller = loader.getController();
            controller.setUser(loggedUser);
            window.setScene(scene);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void loadTransactions() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/views/transaction_history.fxml"));
            Scene scene = new Scene(loader.load(), 900, 600);
            controllers.TransactionHistoryController controller = loader.getController();
            controller.setUser(loggedUser);
            window.setScene(scene);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void loadTransfer() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/views/transfer.fxml"));
            Scene scene = new Scene(loader.load(), 700, 500);
            controllers.TransferController controller = loader.getController();
            controller.setUser(loggedUser);
            window.setScene(scene);
        } catch (Exception e) { e.printStackTrace(); }
    }


    public static void loadPayments() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/views/payments.fxml")); 
            Scene scene = new Scene(loader.load(), 950, 650);
            controllers.PaymentsController controller = loader.getController();
            controller.setUser(loggedUser);
            window.setScene(scene);
        } catch (Exception e) {
            System.out.println("FAILED TO LOAD payments.fxml — FIX FILE NAME OR LOCATION");
            e.printStackTrace();
        }
    }


    public static void loadOpenAccount() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/views/open_account.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            controllers.OpenAccountController controller = loader.getController();
            controller.setUser(loggedUser);
            window.setScene(scene);
        } catch (Exception e) {
            System.out.println("❌ Error loading open_account.fxml");
            e.printStackTrace();
        }
    }

    public static void loadInterest() {
        loadView("/views/interest.fxml", 700, 500);
    }

    public static void logout() {
    loggedUser = null;  
    loadLogin();        
}


    public static User getLoggedUser() {
        return loggedUser;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
