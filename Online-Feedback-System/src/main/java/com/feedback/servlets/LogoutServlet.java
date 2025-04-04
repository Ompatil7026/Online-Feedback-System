package com.feedback.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout") // ✅ General logout for other users
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        try {
            if (session != null) {
                session.invalidate();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        response.sendRedirect("index.html"); // ✅ Redirect to home page
    }
}
