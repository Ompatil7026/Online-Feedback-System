package com.feedback.servlets;

import com.feedback.db.DBConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class UserRegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UserRegistrationServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get form data
        String name = request.getParameter("name");
        String username = request.getParameter("username");  
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Debugging output
        System.out.println("📌 Received Data - Name: " + name + ", Username: " + username + ", Email: " + email);

        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();

            // ✅ Step 1: Check if email already exists
            String checkEmailSQL = "SELECT COUNT(*) FROM users WHERE email = ?";
            checkStmt = conn.prepareStatement(checkEmailSQL);
            checkStmt.setString(1, email);
            rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                System.out.println("❌ Error: Email already exists.");
                response.getWriter().println("<p style='color:red;'>Error: Email already exists. Please use a different email.</p>");
                return; // Stop further execution
            }

            // ✅ Step 2: Insert new user (if email is unique)
            String insertSQL = "INSERT INTO users (name, username, email, password) VALUES (?, ?, ?, ?)";
            insertStmt = conn.prepareStatement(insertSQL);
            insertStmt.setString(1, name);
            insertStmt.setString(2, username);  
            insertStmt.setString(3, email);
            insertStmt.setString(4, password);

            int rowsInserted = insertStmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ Registration successful!");
                response.sendRedirect("login.html"); // Redirect to login page
            } else {
                response.getWriter().println("❌ Registration Failed.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("❌ Database Error: " + e.getMessage());
        } finally {
            // Close resources properly
            try {
                if (rs != null) rs.close();
                if (checkStmt != null) checkStmt.close();
                if (insertStmt != null) insertStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
