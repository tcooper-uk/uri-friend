package tcooper.io;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

import tcooper.io.web.WebApplication;

public class Application {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting...");

        // start server on 8080
        var server = createServer(8080);

        server.setHandler(getRESTEasyHandler());

        server.start();
        server.join();
    }

    /**
     * Create a new Jetty server
     * @param port the port to listen on
     * @return running jetty server
     */
    public static Server createServer(int port) throws Exception {
        Server server = new Server(port);
        return server;
    }

    /**
     * Create a handler for routing to JAX-RS endpoints
     * @return ServletContextHandler
     */
    private static Handler getRESTEasyHandler() {
        ServletContextHandler handler = new ServletContextHandler();

        ServletHolder servlet = handler.addServlet(HttpServletDispatcher.class, "/");

        servlet.setInitParameter("javax.ws.rs.Application", WebApplication.class.getCanonicalName());
        return handler;
    }
}
