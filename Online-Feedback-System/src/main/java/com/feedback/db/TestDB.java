package com.feedback.db;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDB {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                System.out.println("✅ Database Connected Successfully!");
                DBConnection.closeConnection(conn);
            } else {
                System.out.println("❌ Failed to Connect.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
