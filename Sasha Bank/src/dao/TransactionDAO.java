package dao;

import models.Transaction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public static void record(int accountId, String type, double amount, double updatedBalance) {

        try (Connection conn = Database.connect()) {

            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO transactions (account_id, type, amount, date, updated_balance) " +
                    "VALUES (?,?,?,datetime('now'),?)"
            );

            stmt.setInt(1, accountId);
            stmt.setString(2, type);
            stmt.setDouble(3, amount);
            stmt.setDouble(4, updatedBalance);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<Transaction> getHistory(int accountId) {

        List<Transaction> list = new ArrayList<>();

        try (Connection conn = Database.connect()) {

            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM transactions WHERE account_id = ? ORDER BY id DESC"
            );

            stmt.setInt(1, accountId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Transaction(
                        rs.getInt("id"),
                        rs.getInt("account_id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getString("date"),
                        rs.getDouble("updated_balance")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
