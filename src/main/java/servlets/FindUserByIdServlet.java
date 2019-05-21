package servlets;


import datasets.UserDataSet;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.hibernate.ObjectNotFoundException;
import services.DBService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


public class FindUserByIdServlet extends HttpServlet {

    private static final String PAGE = "findUserById.html";
    private final DBService dbService;

    public FindUserByIdServlet(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("user");

        Map<String, Object> map = new HashMap<>();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<ul>");
        if (name != null ) {
            try {
                stringBuilder.append("<li>")
                    .append(dbService.load(Long.valueOf(name), UserDataSet.class))
                    .append("</li>");
            } catch (ObjectNotFoundException ex) {
                stringBuilder.append("No data found").append("</li>");
            }
        }
        stringBuilder.append("</ul>");
        map.put("message", stringBuilder.toString());
        resp.setContentType("text/html;charset=utf-8");
        WebProcessor webProcessor = new WebProcessor();
        resp.getWriter().write(webProcessor.getPage(PAGE, map));
    }

}