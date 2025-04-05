package com.feedback.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/userLogout") // ✅ Fixed mapping conflict
public class UserLogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UserLogoutServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        try {
            if (session != null) {
                session.invalidate(); // Destroy session
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        response.getWriter().println("✅ Logout Successful! Redirecting to login...");
        response.setHeader("Refresh", "2; URL=login.html");
    }
}
