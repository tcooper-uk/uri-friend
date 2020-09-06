package tcooper.io.guice;

import com.google.inject.AbstractModule;
import tcooper.io.web.Endpoint;
import tcooper.io.web.UriShort;

public class ResourceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Endpoint.class);
    bind(UriShort.class);
  }
}
