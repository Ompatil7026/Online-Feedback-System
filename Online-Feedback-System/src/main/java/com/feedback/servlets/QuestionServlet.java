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

@WebServlet("/Questions")
public class QuestionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public QuestionServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            deleteQuestion(request, response);
        } else {
            response.getWriter().println("✅ QuestionServlet is running. Use POST to add questions.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addQuestion(request, response);
        } else if ("delete".equals(action)) {
            deleteQuestion(request, response);
        } else {
            response.getWriter().println("❌ Error: Invalid action.");
        }
    }

    private void addQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String questionText = request.getParameter("question_text");
        String subject = request.getParameter("subject");
        String maxMarksStr = request.getParameter("max_marks");

        if (questionText == null || questionText.trim().isEmpty()) {
            response.getWriter().println("<script>alert('❌ Error: Question text cannot be empty.'); history.back();</script>");
            return;
        }

        if (subject == null || subject.trim().isEmpty()) {
            subject = "General";
        }

        int maxMarks = 0;
        try {
            maxMarks = Integer.parseInt(maxMarksStr);
        } catch (NumberFormatException e) {
            response.getWriter().println("<script>alert('❌ Error: Invalid max marks value.'); history.back();</script>");
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO questions (question_text, subject, max_marks) VALUES (?, ?, ?)");
             PreparedStatement resetStmt = conn.prepareStatement("CALL ResetQuestionIDs()")) {

            insertStmt.setString(1, questionText);
            insertStmt.setString(2, subject);
            insertStmt.setInt(3, maxMarks);
            insertStmt.executeUpdate();

            // Reorder IDs to keep them sequential
            resetStmt.execute();

            response.getWriter().println("<script>alert('✅ Question added and IDs reordered successfully.'); window.location.href=document.referrer;</script>");

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("<script>alert('❌ Database Error: " + e.getMessage() + "'); history.back();</script>");
        }
    }

    private void deleteQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idStr = request.getParameter("id");

        if (idStr == null || idStr.trim().isEmpty()) {
            response.getWriter().println("<script>alert('❌ Error: Invalid question ID.'); history.back();</script>");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.getWriter().println("<script>alert('❌ Error: Invalid question ID format.'); history.back();</script>");
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM questions WHERE id=?");
             PreparedStatement resetStmt = conn.prepareStatement("CALL ResetQuestionIDs()")) {

            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                resetStmt.execute(); // Reorder IDs after deletion
                response.getWriter().println("<script>alert('✅ Question deleted and IDs reordered successfully.'); window.location.href='manageQuestions';</script>");
            } else {
                response.getWriter().println("<script>alert('❌ Error: Question not found.'); history.back();</script>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("<script>alert('❌ Database Error: " + e.getMessage() + "'); history.back();</script>");
        }
    }
}