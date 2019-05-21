package servlets;

import datasets.UserDataSet;
import org.hibernate.ObjectNotFoundException;
import services.DBService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddNewUserServlet extends HttpServlet {

    private static final String PAGE = "addNewUser.html";
    private final DBService dbService;

    public AddNewUserServlet(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String age = req.getParameter("age");
        Map<String, Object> map = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<ul>").append("<li>");
        if (name != null && age != null) {
            UserDataSet userDataSet = new UserDataSet(name, age);
            dbService.save(userDataSet);
            stringBuilder.append(userDataSet.getId());
        }
        stringBuilder.append("</li>").append("</ul>");
        map.put("message", stringBuilder.toString());
        resp.setContentType("text/html;charset=utf-8");
        WebProcessor webProcessor = new WebProcessor();
        resp.getWriter().write(webProcessor.getPage(PAGE, map));
    }

}