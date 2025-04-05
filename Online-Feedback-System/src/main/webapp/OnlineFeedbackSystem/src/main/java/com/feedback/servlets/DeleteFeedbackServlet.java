package com.feedback.servlets;

import com.feedback.db.DBConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DeleteFeedbackServlet")
public class DeleteFeedbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int feedbackId = Integer.parseInt(request.getParameter("id"));

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "DELETE FROM feedbacks WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, feedbackId);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                response.sendRedirect("viewResults");
            } else {
                response.getWriter().println("Error deleting feedback.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Database Error: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
    }
}
