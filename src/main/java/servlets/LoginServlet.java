package servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends HttpServlet {

    private static final String PAGE = "login.html"; // seconds
    private static final String LOGIN = "John";
    private static final String PASSWORD = "qwerty";

    public LoginServlet() {}

    private boolean authenticate(final String login, final String password) {
        return LOGIN.equals(login) && PASSWORD.equals(password);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String name = request.getParameter("login");
        String password = request.getParameter("password");
        Map<String, Object> map = new HashMap<>();

        response.setContentType("text/html;charset=utf-8");
        WebProcessor webProcessor = new WebProcessor();
        response.getWriter().write(webProcessor.getPage(PAGE, map));

        if (authenticate(name, password)) {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(30);
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("<html xmlns=\"http://www.w3.org/1999/xhtml\" content=\"text/html;\">\n" +
                    "<body>\n" +
                    "<div class=\"top\">\n" +
                    "    <br/>\n" +
                    "    <a href=\"findUserById\">FindUserByID</a>\n" +
                    "<br/>" +
                    "    <a href=\"getAllUsers\">GetAllUsers</a>\n" +
                    "<br/>" +
                    "    <a href=\"addNewUser\">AddNewUser</a>\n" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>");
        } else {
            response.setStatus(403);
        }

    }

}