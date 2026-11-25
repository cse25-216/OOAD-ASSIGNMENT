package dao;

import models.Company;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class CompanyDAO {

    public static void createCompany(Company company) {

        String sql = "INSERT INTO companies (name, business_id, reg_number, reg_date, capital) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, company.getName());
            ps.setString(2, company.getBusinessId());
            ps.setString(3, company.getRegistrationNumber());
            ps.setString(4, company.getRegistrationDate());
            ps.setDouble(5, company.getStartingCapital());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
