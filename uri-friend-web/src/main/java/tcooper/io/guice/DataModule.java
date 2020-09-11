package tcooper.io.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.zaxxer.hikari.HikariDataSource;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Qualifier;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import tcooper.io.database.JdbiRepository;
import tcooper.io.database.UriRepository;

public class DataModule extends AbstractModule {

  private static final String JDBC_URL = "jdbc:postgresql:uri_short";

  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  public @interface JdbcUrl {}

  @Provides
  @JdbcUrl
  static String providesJdbcUrl() {
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

  private DataSource getDataSource(){
    HikariDataSource ds = new HikariDataSource();
    ds.setJdbcUrl(JDBC_URL);
    return ds;
  }

}
