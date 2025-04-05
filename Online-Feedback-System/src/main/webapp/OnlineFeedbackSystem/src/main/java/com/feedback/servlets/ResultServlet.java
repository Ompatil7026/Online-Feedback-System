package com.feedback.servlets;

import com.feedback.db.DBConnection;
import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/viewResults")
public class ResultServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || (session.getAttribute("userId") == null && session.getAttribute("admin") == null)) {
            response.sendRedirect("login.html");
            return;
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            boolean isAdmin = session.getAttribute("admin") != null;
            String sql;

            if (isAdmin) {
                sql = "SELECT u.name, COALESCE(q.question_text, 'Unknown Question') AS question_text, " +
                      "f.submission_date, f.rating " +
                      "FROM feedbacks f " +
                      "LEFT JOIN questions q ON f.question_id = q.id " +
                      "JOIN users u ON f.user_id = u.id";
                stmt = conn.prepareStatement(sql);
            } else {
                sql = "SELECT COALESCE(q.question_text, 'Unknown Question') AS question_text, " +
                      "f.submission_date, f.rating " +
                      "FROM feedbacks f " +
                      "LEFT JOIN questions q ON f.question_id = q.id " +
                      "WHERE f.user_id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, (int) session.getAttribute("userId"));
            }

            rs = stmt.executeQuery();

            // Include Bootstrap and External CSS
            out.println("<html><head><title>Feedback Results</title>");
            out.println("<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css'>");
            out.println("<link rel='stylesheet' href='css/styles.css'>");
            out.println("</head><body>");
            
            // Container for Responsive Design
            out.println("<div class='container mt-5'>");
            out.println("<h2 class='text-center text-primary'>Feedback Results</h2>");
            out.println("<table class='table table-bordered table-hover mt-3'>");
            
            // Table Header
            if (isAdmin) {
                out.println("<thead class='table-dark'><tr><th>User</th><th>Question</th><th>Submission Date</th><th>Rating</th></tr></thead>");
            } else {
                out.println("<thead class='table-dark'><tr><th>Question</th><th>Submission Date</th><th>Rating</th></tr></thead>");
            }

            out.println("<tbody>");
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                out.println("<tr>");
                if (isAdmin) {
                    out.println("<td>" + rs.getString("name") + "</td>");
                }
                out.println("<td>" + rs.getString("question_text") + "</td>");
                out.println("<td>" + rs.getTimestamp("submission_date") + "</td>");

                int rating = rs.getInt("rating");
                out.println("<td>" + (rs.wasNull() ? "N/A" : rating) + "</td>");
                out.println("</tr>");
            }

            if (!hasData) {
                out.println("<tr><td colspan='" + (isAdmin ? "4" : "3") + "' class='text-center text-danger'>No feedback available.</td></tr>");
            }

            out.println("</tbody></table>");

            // Back Button
            out.println("<div class='text-center mt-4'>");
            out.println("<a href='admin_dashboard.html' class='btn btn-success'>⬅ Back to Dashboard</a>");
            out.println("</div>");

            out.println("</div>"); // Close container
            out.println("</body></html>");
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<p class='text-center text-danger'>Error: " + e.getMessage() + "</p>");
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
