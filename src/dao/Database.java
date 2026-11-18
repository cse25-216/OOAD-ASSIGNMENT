package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String DB_URL = "jdbc:sqlite:sasha_bank.db";
    private static boolean initialized = false;

    public static synchronized Connection connect() {
        try {
            Class.forName("org.sqlite.JDBC");

            Connection conn = DriverManager.getConnection(DB_URL);

            try {
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON");
                }

                if (!initialized) {
                    createTables(conn);
                    migrateTransactionsTable(conn);
                    initialized = true;
                }

                return conn;

            } catch (SQLException e) {
                conn.close();
                throw e;
            }

        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                "SQLite JDBC driver not found. Check the jGRASP classpath.",
                e
            );

        } catch (SQLException e) {
            throw new IllegalStateException(
                "Could not connect to Sasha Bank database.",
                e
            );
        }
    }

    public static Connection getConnection() {
        return connect();
    }

    private static void createTables(Connection conn)
            throws SQLException {

        try (Statement stmt = conn.createStatement()) {

            String users =
                "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "full_name TEXT NOT NULL," +
                "username TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "role TEXT NOT NULL" +
                ")";

            String accounts =
                "CREATE TABLE IF NOT EXISTS accounts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "type TEXT NOT NULL," +
                "balance REAL NOT NULL," +
                "FOREIGN KEY (user_id) REFERENCES users(id)" +
                ")";

            String transactions =
                "CREATE TABLE IF NOT EXISTS transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "account_id INTEGER NOT NULL," +
                "type TEXT NOT NULL," +
                "amount REAL NOT NULL," +
                "date TEXT NOT NULL," +
                "updated_balance REAL," +
                "FOREIGN KEY (account_id) REFERENCES accounts(id)" +
                ")";

            String companies =
                "CREATE TABLE IF NOT EXISTS companies (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "business_id TEXT NOT NULL," +
                "reg_number TEXT NOT NULL," +
                "reg_date TEXT NOT NULL," +
                "capital REAL NOT NULL" +
                ")";

            stmt.execute(users);
            stmt.execute(accounts);
            stmt.execute(transactions);
            stmt.execute(companies);

            createDefaultAdmin(conn);
        }
    }

    private static void migrateTransactionsTable(Connection conn)
            throws SQLException {

        boolean hasUpdatedBalance = false;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "PRAGMA table_info(transactions)"
             )) {

            while (rs.next()) {
                if ("updated_balance".equalsIgnoreCase(
                        rs.getString("name"))) {
                    hasUpdatedBalance = true;
                    break;
                }
            }
        }

        if (!hasUpdatedBalance) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(
                    "ALTER TABLE transactions " +
                    "ADD COLUMN updated_balance REAL"
                );
            }
        }
    }

    private static void createDefaultAdmin(Connection conn)
            throws SQLException {

        String sql =
            "INSERT OR IGNORE INTO users " +
            "(full_name, username, password, role) " +
            "VALUES (" +
            "'System Administrator', " +
            "'admin', " +
            "'admin123', " +
            "'ADMIN'" +
            ")";

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}
