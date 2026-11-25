package dao;

import models.User;
import java.sql.*;

public class UserDAO {

    public User findUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; 
    }
    public boolean insertUser(User user) {
        String sql = "INSERT INTO users(full_name, username, password, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    
    
    public int insertUserReturnID(User user) {
    String sql = "INSERT INTO users(full_name, username, password, role) VALUES (?, ?, ?, ?)";

    try (Connection conn = Database.connect();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        stmt.setString(1, user.getFullName());
        stmt.setString(2, user.getUsername());
        stmt.setString(3, user.getPassword());
        stmt.setString(4, user.getRole());

        int affected = stmt.executeUpdate();

        if (affected == 0) return -1;

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) return rs.getInt(1);

    } catch (Exception e) {
        e.printStackTrace();
    }

    return -1;
}
public boolean createInitialAccount(int userId, String type, double balance) {
    String sql = "INSERT INTO accounts(user_id, type, balance) VALUES (?, ?, ?)";

    try (Connection conn = Database.connect();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, userId);
        stmt.setString(2, type);
        stmt.setDouble(3, balance);

        return stmt.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

}
