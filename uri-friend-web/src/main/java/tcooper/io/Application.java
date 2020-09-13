package tcooper.io;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import java.io.IOException;
import java.util.Properties;
import javax.sql.DataSource;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.flywaydb.core.Flyway;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tcooper.io.guice.ConfigurationModule;
import tcooper.io.guice.DataModule;
import tcooper.io.guice.JettyModule;
import tcooper.io.guice.JettyModule.Port;
import tcooper.io.guice.ResourceModule;
import tcooper.io.guice.RestEasyModule;

public class Application {

  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

  private final GuiceFilter filter;
  private final GuiceResteasyBootstrapServletContextListener listener;
  private final int port;
  private final DataSource dataSource;
  private final Properties properties;

  @Inject
  public Application(GuiceFilter filter,
      GuiceResteasyBootstrapServletContextListener listener,
      @Port int port, DataSource dataSource, Properties properties) {
    this.filter = filter;
    this.listener = listener;
    this.port = port;
    this.dataSource = dataSource;
    this.properties = properties;
  }

  /**
   * Bootstrap our application
   * - setup the Guice container
   * @return Guice injector
   * @throws IOException - fail fast if we have no properties
   */
  private static Injector bootstrap() throws IOException {

    // bootstrap the application
    return Guice.createInjector(
        new ConfigurationModule(),
        new JettyModule(),
        new RestEasyModule(),
        new DataModule(),
        new ResourceModule()
    );
  }

  /**
   * Start up the the application
   * - Setup database schema
   * - Spin up HTTP server
   * @throws Exception - Fail fast.
   */
  public void run() throws Exception {
    LOGGER.info("Starting...");

    if (properties == null) {
      throw new RuntimeException("Application has no configuration. Panic.");
    }

    // setup db
    processFlyway();

    // start server
    var server = createServer(port);
    server.start();
    server.join();

    LOGGER.info("Stopping...");
  }

  /**
   * Run the flyway migration for the database.
   */
  private void processFlyway() {

    Flyway
        .configure()
        .createSchemas(true)
        .baselineOnMigrate(true)
        .dataSource(dataSource)
        .load();
  }

  /**
   * Create a new Jetty server
   *
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
   *
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

  /**
   * Main.
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    bootstrap().getInstance(Application.class).run();
  }
}
