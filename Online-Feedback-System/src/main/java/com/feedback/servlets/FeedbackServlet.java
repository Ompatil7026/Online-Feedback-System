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
import java.io.PrintWriter;

@WebServlet("/feedbackForm")
public class FeedbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public FeedbackServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.getWriter().println("❌ Please login first!");
            response.setHeader("Refresh", "2; URL=login.html");
            return;
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Submit Feedback</title>");
        out.println("<link rel='stylesheet' href='styles.css'>"); // Link to external CSS
        out.println("<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css'>"); // Bootstrap
        out.println("<style>");
        out.println("body { background-color: #f8f9fa; font-family: Arial, sans-serif; }");
        out.println(".container { max-width: 600px; margin-top: 50px; background: white; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }");
        out.println("h2 { text-align: center; color: #007bff; }");
        out.println("label { font-weight: bold; }");
        out.println("button { width: 100%; }");
        out.println(".logout-btn { display: block; text-align: center; margin-top: 20px; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        out.println("<div class='container'>");
        out.println("<h2>Submit Your Feedback</h2>");
        out.println("<form action='submitFeedback' method='post'>");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM questions");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int questionId = rs.getInt("id");
                String questionText = rs.getString("question_text");
                String subject = rs.getString("subject");
                int maxMarks = rs.getInt("max_marks");

                out.println("<div class='mb-3'>");
                out.println("<label class='form-label'>" + subject + ": " + questionText + " (Max: " + maxMarks + ")</label>");
                out.println("<input type='number' class='form-control' name='q" + questionId + "' min='1' max='" + maxMarks + "' required>");
                out.println("</div>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<div class='alert alert-danger'>Error fetching questions: " + e.getMessage() + "</div>");
        }

        out.println("<button type='submit' class='btn btn-primary'>Submit Feedback</button>");
        out.println("</form>");
        out.println("<a href='logout' class='btn btn-danger logout-btn'>Logout</a>");
        out.println("</div>");

        out.println("</body>");
        out.println("</html>");
    }
}
