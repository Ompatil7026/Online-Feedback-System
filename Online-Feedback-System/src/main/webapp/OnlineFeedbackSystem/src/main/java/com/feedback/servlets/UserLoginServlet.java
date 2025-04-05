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
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class UserLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UserLoginServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            // Fetch ID and Name of user
            String sql = "SELECT id, name FROM users WHERE email = ? AND password = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                String userName = rs.getString("name");  // Fetch user's name
                
                // Store user data in session
                HttpSession session = request.getSession();
                session.setAttribute("user_id", userId);
                session.setAttribute("user_name", userName);
                session.setAttribute("user_email", email);

                response.sendRedirect("feedbackForm"); // Redirect to feedback form
            } else {
                response.getWriter().println("❌ Invalid Email or Password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("❌ Database Error: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
