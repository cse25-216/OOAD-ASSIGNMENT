package dao;
public class DatabaseSetup {
    public static void initialize() {
        try (java.sql.Connection connection = Database.getConnection()) {
            if (connection == null) throw new IllegalStateException("Database initialization failed");
        } catch (java.sql.SQLException e) { throw new IllegalStateException(e); }
    }
}
