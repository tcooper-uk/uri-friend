package tcooper.io;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.internal.cglib.proxy.$MethodProxy;
import com.google.inject.servlet.GuiceFilter;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import tcooper.io.guice.DataModule;
import tcooper.io.guice.JettyModule;
import tcooper.io.guice.JettyModule.Port;
import tcooper.io.guice.ResourceModule;
import tcooper.io.guice.RestEasyModule;

public class Application {

    private final GuiceFilter filter;
    private final GuiceResteasyBootstrapServletContextListener listener;
    private final int port;

    @Inject
    public Application(GuiceFilter filter,
        GuiceResteasyBootstrapServletContextListener listener,
        @Port int port) {
        this.filter = filter;
        this.listener = listener;
        this.port = port;
    }

    private static Injector bootstrap() {
        return Guice.createInjector(
            new JettyModule(),
            new RestEasyModule(),
            new DataModule(),
            new ResourceModule()
        );
    }

    public void run() throws Exception {
        System.out.println("Starting...");

        // start server
        var server = createServer(port);
        server.start();
        server.join();

        System.out.println("Stopping...");
    }

    /**
     * Create a new Jetty server
     * @param port the port to listen on
     * @return running jetty server
     */
    private Server createServer(int port) {
        var server = new Server(port);
        server.setHandler(getHandler());
        return server;
    }

    /**
     * Create a handler for routing to JAX-RS endpoints
     * @return ServletContextHandler
     */
    private Handler getHandler() {
        ServletContextHandler handler = new ServletContextHandler();

        // filter all requests through guice
        FilterHolder filterHolder = new FilterHolder(filter);
        handler.addFilter(filterHolder, "/*", null);

        // RESTEasy listener for requests dispatched from guice
        handler.addEventListener(listener);

        // fallback servlet
        handler.addServlet(DefaultServlet.class, "/");

        return handler;
    }

    public static void main(String[] args) throws Exception {
        bootstrap().getInstance(Application.class).run();
    }
}
