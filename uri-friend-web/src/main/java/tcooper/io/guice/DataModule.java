package tcooper.io.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Qualifier;
import tcooper.io.database.JdbiRepository;
import tcooper.io.database.UriRepository;

public class DataModule extends AbstractModule {

  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  public @interface JdbcUrl {}

  @Provides
  @JdbcUrl
  static String providesJdbcUrl() {
    return "jdbc:postgresql:uri_short";
  }

  @Override
  protected void configure() {
    bind(UriRepository.class).to(JdbiRepository.class);
  }
}
