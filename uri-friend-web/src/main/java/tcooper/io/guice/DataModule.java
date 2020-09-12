package tcooper.io.guice;

import com.google.common.base.Strings;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.zaxxer.hikari.HikariDataSource;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Properties;
import javax.inject.Qualifier;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import tcooper.io.database.JdbiRepository;
import tcooper.io.database.UriRepository;

public class DataModule extends AbstractModule {

  private static String JDBC_PROP_NAME = "app.database.jdbc.url";

  private final String JDBC_URL;

  public DataModule(Properties properties) {

    String url = properties.getProperty(JDBC_PROP_NAME);

    if (Strings.isNullOrEmpty(url)) {
      throw new IllegalStateException("No JDBC URL found.");
    }

    JDBC_URL = url;
  }

  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  public @interface JdbcUrl { }

  @Provides
  @JdbcUrl
  String providesJdbcUrl() {
    return JDBC_URL;
  }

  @Override
  protected void configure() {
    DataSource ds = getDataSource();
    bind(DataSource.class).toInstance(ds);
    bind(UriRepository.class).to(JdbiRepository.class);

    bind(Flyway.class).toInstance(Flyway
        .configure()
        .createSchemas(true)
        .baselineOnMigrate(true)
        .dataSource(ds)
        .load()
    );
  }

  private DataSource getDataSource() {
    HikariDataSource ds = new HikariDataSource();
    ds.setJdbcUrl(JDBC_URL);
    return ds;
  }

}
