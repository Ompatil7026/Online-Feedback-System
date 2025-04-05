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
import javax.servlet.http.HttpSession;

@WebServlet("/submitFeedback")
public class SubmitFeedbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public SubmitFeedbackServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("user_id") == null) {
        response.getWriter().println("❌ Error: User not logged in.");
        return;
    }

    int userId = (int) session.getAttribute("user_id");
    String userName = (String) session.getAttribute("user_name");

    Connection conn = null;
    PreparedStatement stmt = null;

    try {
        conn = DBConnection.getConnection();
        String sql = "INSERT INTO feedbacks (user_id, name, submission_date, question_id, rating) VALUES (?, ?, NOW(), ?, ?)";
        stmt = conn.prepareStatement(sql);

        // Loop through request parameters to capture multiple feedback entries
        for (String paramName : request.getParameterMap().keySet()) {
            if (paramName.startsWith("q")) {  // Question format: q1, q2, q3...
                int questionId = Integer.parseInt(paramName.substring(1));
                int rating = Integer.parseInt(request.getParameter(paramName));

                stmt.setInt(1, userId);
                stmt.setString(2, userName);
                stmt.setInt(3, questionId);
                stmt.setInt(4, rating);
                stmt.addBatch();  // Add batch for efficiency
            }
        }

        stmt.executeBatch(); // Execute all insert queries
        response.sendRedirect("thankyou.html?name=" + java.net.URLEncoder.encode(userName, "UTF-8"));

    } catch (SQLException e) {
        e.printStackTrace();
        response.getWriter().println("❌ Database Error: " + e.getMessage());
    } finally {
        try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

}
