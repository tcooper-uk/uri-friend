package tcooper.io.guice;

import com.google.inject.Provides;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.ServletModule;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Qualifier;

public class JettyModule extends ServletModule {

  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Port {}

  @Provides
  @Port
  public int providesPort() {
    return 8080;
  }

  @Override
  protected void configureServlets() {
    bind(GuiceFilter.class);
  }
}
