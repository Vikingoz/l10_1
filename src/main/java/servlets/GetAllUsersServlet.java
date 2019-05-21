package servlets;

import datasets.UserDataSet;
import services.DBService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllUsersServlet  extends HttpServlet {

    private static final String PAGE = "getAllUsers.html";
    private final DBService dbService;

    public GetAllUsersServlet(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> map = new HashMap<>();
        StringBuilder s = new StringBuilder();
        s.append("<ul>");
        for(UserDataSet userDataSet: dbService.loadAll(UserDataSet.class)) {
            s.append("<li>").append(userDataSet.toString()).append("</li>");
        }
        map.put("message",  s.append("</ul>").toString());

        resp.setContentType("text/html;charset=utf-8");
        WebProcessor webProcessor = new WebProcessor();
        resp.getWriter().write(webProcessor.getPage(PAGE, map));
    }

}