package webpage;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import services.DBService;
import servlets.AddNewUserServlet;
import servlets.GetAllUsersServlet;
import servlets.LoginServlet;
import servlets.FindUserByIdServlet;


public class WebPage {
    private final static int PORT = 8080;
    private final DBService service;

    public WebPage(DBService service) {
        this.service = service;
    }

    public void start() throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(new FindUserByIdServlet(getService())), "/findUserById");
        context.addServlet(new ServletHolder(new GetAllUsersServlet(getService())), "/getAllUsers");
        context.addServlet(new ServletHolder(new AddNewUserServlet(getService())), "/addNewUser");
        context.addServlet(new ServletHolder(new LoginServlet()), "/login");

        context.addFilter(new FilterHolder(new AuthorizationFilter()), "/findUserById", null);
        context.addFilter(new FilterHolder(new AuthorizationFilter()), "/getAllUsers", null);
        context.addFilter(new FilterHolder(new AuthorizationFilter()), "/addNewUser", null);

        Server server = new Server(PORT);
        server.setHandler(new HandlerList(context));

        server.start();
        server.join();
    }

    public DBService getService() {
        return service;
    }
}