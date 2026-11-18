package services;

import dao.AccountDAO;
import dao.Database;
import models.Account;
import models.Withdrawable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountService {

    private static final AccountDAO accountDAO = new AccountDAO();

    public static boolean withdraw(Account account, double amount) {

        if (!(account instanceof Withdrawable))
            return false;

        if (!Double.isFinite(amount) || amount <= 0 ||
                amount > account.getBalance())
            return false;

        ((Withdrawable) account).withdraw(amount);

        accountDAO.updateBalance(
                account.getId(),
                account.getBalance()
        );

        return true;
    }

    public static boolean deposit(Account account, double amount) {

        if (!Double.isFinite(amount) || amount <= 0)
            return false;

        account.deposit(amount);

        accountDAO.updateBalance(
                account.getId(),
                account.getBalance()
        );

        return true;
    }

    public static boolean transfer(Account from, Account to, double amount) {

        if (from == null || to == null)
            return false;

        if (from.getId() == to.getId())
            return false;

        if (!Double.isFinite(amount) || amount <= 0)
            return false;

        if (!(from instanceof Withdrawable))
            return false;

        try (Connection conn = Database.getConnection()) {

            conn.setAutoCommit(false);

            try {
                double fromBalance = getBalance(conn, from.getId());
                double toBalance = getBalance(conn, to.getId());

                if (fromBalance < amount) {
                    conn.rollback();
                    return false;
                }

                double newFromBalance = fromBalance - amount;
                double newToBalance = toBalance + amount;

                updateBalance(conn, from.getId(), newFromBalance);
                updateBalance(conn, to.getId(), newToBalance);

                recordTransaction(
                        conn,
                        from.getId(),
                        "Transfer to " + to.getType(),
                        amount,
                        newFromBalance
                );

                recordTransaction(
                        conn,
                        to.getId(),
                        "Transfer from " + from.getType(),
                        amount,
                        newToBalance
                );

                conn.commit();
                return true;

            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static double getBalance(Connection conn, int accountId)
            throws SQLException {

        String sql = "SELECT balance FROM accounts WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);

            try (ResultSet rs = stmt.executeQuery()) {

                if (!rs.next()) {
                    throw new SQLException("Account not found: " + accountId);
                }

                return rs.getDouble("balance");
            }
        }
    }

    private static void updateBalance(
            Connection conn,
            int accountId,
            double balance
    ) throws SQLException {

        String sql =
                "UPDATE accounts SET balance = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, balance);
            stmt.setInt(2, accountId);

            if (stmt.executeUpdate() != 1) {
                throw new SQLException("Account balance update failed.");
            }
        }
    }

    private static void recordTransaction(
            Connection conn,
            int accountId,
            String type,
            double amount,
            double updatedBalance
    ) throws SQLException {

        String sql =
                "INSERT INTO transactions " +
                "(account_id, type, amount, date, updated_balance) " +
                "VALUES (?, ?, ?, datetime('now'), ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            stmt.setString(2, type);
            stmt.setDouble(3, amount);
            stmt.setDouble(4, updatedBalance);

            if (stmt.executeUpdate() != 1) {
                throw new SQLException("Transaction recording failed.");
            }
        }
    }
}
