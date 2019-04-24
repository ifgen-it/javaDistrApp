package web;

import ejb.UserService;
import java.io.IOException;
import java.io.PrintWriter;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "IndexServlet", urlPatterns = {"/IndexServlet"})
public class IndexServlet extends HttpServlet {

    private UserService userService;
    boolean loginResult;
    HttpSession session;
    String login;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        session = request.getSession();
        boolean sessionNew = session.isNew();

        if (sessionNew) {
            try {
                InitialContext ic = new InitialContext();
                userService = (UserService) ic.lookup("java:global/Task5EJB/Task5EJB-ejb/UserServiceBean!ejb.UserService");
                session.setAttribute("bean", userService);

            } catch (NamingException ex) {
                System.out.println("-----> initial context err: " + ex.getMessage());
            }
        } else {
            userService = (UserService) session.getAttribute("bean");
        }

        if (request.getParameter("go") != null) {
            login = request.getParameter("login");
            String psw = request.getParameter("psw");
            loginResult = userService.login(login, psw);
        }
        if (userService.getLoginStatus() == false) {
            String msg = userService.getMessage();
            loginFail(request, response, msg);
            return;
        }

        String message = null;
        if (request.getParameter("getmessage") != null) {
            message = userService.getMessage();
        }


        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Server side</title>");
            out.println("</head>");
            out.println("<body style=\"background: #00cc99; color: #006600; font-size: 18px\">");
            out.println("<div align=\"center\">");
            out.println("<h2>");

            String temp = login.toLowerCase();
            String end = temp.substring(1);
            String beg = temp.substring(0, 1).toUpperCase();
            String Login = beg + end;
            out.println(Login + ", welcome to the website!<br>");

            out.println("</h2>");

            out.println("<button onclick=\"window.location.href='index.html';\">Start page</button>");

            out.println("<h3>");
            out.println("<form method=\"post\">\n"
                    + "<input type=\"submit\" value=\"Get message\" name=\"getmessage\"><br>\n"
                    + "</form><br>");
            if (message != null) {
                out.println("Message : " + message);
            }

            out.println("</h3>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }

    }

    protected void loginFail(HttpServletRequest request, HttpServletResponse response, String msg)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Server side</title>");
            out.println("</head>");
            out.println("<body style=\"background: #ff6666; color: #990000; font-size: 18px\">");

            out.println("<div align=\"center\">");
            out.println("<h2>");
            out.println(msg + "<br>");
            out.println("</h2>");

            out.println("<button onclick=\"window.location.href='index.html';\">Start page</button>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

        }

    }

}
