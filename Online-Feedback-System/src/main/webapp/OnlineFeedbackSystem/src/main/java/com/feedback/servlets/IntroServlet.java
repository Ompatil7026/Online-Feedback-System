package com.feedback.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@WebServlet("/intro") // ✅ Maps this servlet to "/intro"
public class IntroServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public IntroServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Welcome - Online Feedback System</title>");
        out.println("<link rel='stylesheet' href='styles.css'>"); // Link external CSS
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<header>");
        out.println("<h1>Welcome to the Online Feedback System</h1>");
        out.println("<p>The Online Feedback System is a web-based application designed to collect, manage, and analyze feedback efficiently.</p>");
        out.println("</header>");

        out.println("<section class='about'>");
        out.println("<h2>How It Works?</h2>");
        out.println("<ul>");
        out.println("<li>📌 <b>Users:</b> Register, log in, and answer feedback questions.</li>");
        out.println("<li>📌 <b>Admins:</b> Manage questions, assign marks, view feedback responses, and analyze reports.</li>");
        out.println("<li>📌 <b>Technology:</b> Built with HTML, CSS, Java Servlets, and MySQL.</li>");
        out.println("</ul>");
        out.println("</section>");

        out.println("<nav class='buttons'>");
        out.println("<h2>Get Started</h2>");
        out.println("<a href='register.html' class='btn' aria-label='Register as a new user'>User Registration</a>");
        out.println("<a href='login.html' class='btn' aria-label='Login as an existing user'>User Login</a>");
        out.println("<a href='admin_login.html' class='btn admin-btn' aria-label='Admin login panel'>Admin Login</a>");
        out.println("</nav>");

        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}
