package tcooper.io.guice;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

public class RestEasyModule extends ServletModule {

  @Override
  protected void configureServlets() {
    bind(GuiceResteasyBootstrapServletContextListener.class);
    bind(HttpServletDispatcher.class).in(Singleton.class);

    // server all paths with rest easy
    serve("/*").with(HttpServletDispatcher.class);
  }
}
