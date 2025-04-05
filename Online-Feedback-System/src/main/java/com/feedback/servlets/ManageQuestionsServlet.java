
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

@WebServlet("/manageQuestions")
public class ManageQuestionsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect("login.html");
            return;
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT id, question_text, subject, max_marks FROM questions ORDER BY id DESC";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            out.println("<html><head><title>Manage Questions</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 20px; background: #f4f4f4; text-align: center; }");
            out.println("h2, h3 { color: #333; }");
            out.println(".container { max-width: 800px; margin: auto; background: white; padding: 20px; border-radius: 10px; box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); }");
            out.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
            out.println("table, th, td { border: 1px solid #ddd; }");
            out.println("th, td { padding: 10px; text-align: center; }");
            out.println("th { background: #007BFF; color: white; }");
            out.println("tr:nth-child(even) { background: #f2f2f2; }");
            out.println("tr:hover { background: #ddd; }");
            out.println("form { margin-top: 20px; }");
            out.println("input, button { padding: 10px; margin: 5px; border-radius: 5px; border: 1px solid #ccc; width: 80%; }");
            out.println("button { background: #28a745; color: white; cursor: pointer; }");
            out.println("button:hover { background: #218838; }");
            out.println(".delete-btn { background: #dc3545; color: white; text-decoration: none; padding: 5px 10px; border-radius: 5px; }");
            out.println(".delete-btn:hover { background: #c82333; }");
            out.println("</style>");
            out.println("</head><body>");
            
            out.println("<div class='container'>");
            out.println("<h2>Manage Questions</h2>");

            // Display existing questions
            out.println("<table>");
            out.println("<tr><th>ID</th><th>Question</th><th>Subject</th><th>Max Marks</th><th>Action</th></tr>");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("id") + "</td>");
                out.println("<td>" + rs.getString("question_text") + "</td>");
                out.println("<td>" + rs.getString("subject") + "</td>");
                out.println("<td>" + rs.getInt("max_marks") + "</td>");
                out.println("<td><a href='Questions?action=delete&id=" + rs.getInt("id") + "' class='delete-btn'>❌ Delete</a></td>");
                out.println("</tr>");
            }
            out.println("</table>");

            // Form to add new questions
            out.println("<h3>Add New Question</h3>");
            out.println("<form action='Questions' method='POST'>");
            out.println("<input type='hidden' name='action' value='add'>");
            out.println("<input type='text' name='question_text' placeholder='Enter question' required>");
            out.println("<input type='text' name='subject' placeholder='Enter subject' onblur='setDefaultSubject(this)'>");
            out.println("<input type='number' name='max_marks' placeholder='Enter max marks' required>");
            out.println("<button type='submit'>➕ Add Question</button>");
            out.println("</form>");

            out.println("<br><a href='admin_dashboard.html'>⬅ Back to Dashboard</a>");

            // JavaScript to set default subject
            out.println("<script>");
            out.println("function setDefaultSubject(input) {");
            out.println("    if (input.value.trim() === '') {");
            out.println("        input.value = 'General';");
            out.println("    }");
            out.println("}");
            out.println("</script>");

            out.println("</div>"); // Closing .container
            out.println("</body></html>");
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<p style='color:red; text-align:center;'>Error: " + e.getMessage() + "</p>");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    } // **Closing doGet method**
} // **Closing class ManageQuestionsServlet**
