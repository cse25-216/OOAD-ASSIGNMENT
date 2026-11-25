package dao;

import models.Account;
import models.ChequingAccount;
import models.InvestorsAccount;
import models.SavingsAccount;

import java.sql.*;

public class AccountDAO {

    public Account getAccount(int userId, String type) {

        try (Connection conn = Database.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM accounts WHERE user_id = ? AND type = ?");

            stmt.setInt(1, userId);
            stmt.setString(2, type);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                double balance = rs.getDouble("balance");

                switch (type) {
                    case "Savings":
                        return new SavingsAccount(id, userId, balance);
                    case "Chequing":
                        return new ChequingAccount(id, userId, balance);
                    case "Investors":
                        return new InvestorsAccount(id, userId, balance);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getUserBalance(int userId) {

        String sql = "SELECT balance FROM accounts WHERE user_id = ? LIMIT 1";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return rs.getDouble("balance");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    public boolean createAccount(int userId, String type) {
        try (Connection conn = Database.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO accounts (user_id, type, balance) VALUES (?, ?, 0)");

            stmt.setInt(1, userId);
            stmt.setString(2, type);

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createInitialAccount(int userId, String type, double startingAmount) {

        try (Connection conn = Database.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO accounts (user_id, type, balance) VALUES (?, ?, ?)");

            stmt.setInt(1, userId);
            stmt.setString(2, type);
            stmt.setDouble(3, startingAmount);

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createAccount(int userId, String type, double balance) {
        return createInitialAccount(userId, type, balance);
    }

    public void updateBalance(int accountId, double newBalance) {

        try (Connection conn = Database.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE accounts SET balance = ? WHERE id = ?");

            stmt.setDouble(1, newBalance);
            stmt.setInt(2, accountId);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
