package tcooper.io.guice;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Properties;
import javax.sql.DataSource;

public class DataSourceProvider implements Provider<DataSource> {

  private static final String JDBC_PROP_NAME = "app.database.jdbc.url";
  private static final String DB_USER_PROP_NAME = "app.database.user";
  private static final String DB_PASSWORD_PROP_NAME = "app.database.password";
  private final Properties properties;

  private static DataSource dataSource = null;

  @Inject
  public DataSourceProvider(Properties properties) {
    this.properties = properties;
  }

  @Override
  public DataSource get() {

    // cache, we only want one.
    if(dataSource != null) return dataSource;

    HikariDataSource ds = new HikariDataSource();
    ds.setJdbcUrl(getJdbcUrl());

    String user = properties.getProperty(DB_USER_PROP_NAME);
    String password = properties.getProperty(DB_PASSWORD_PROP_NAME);

    if(!Strings.isNullOrEmpty(user) &&
    !Strings.isNullOrEmpty(password)) {
      ds.setUsername(user);
      ds.setPassword(password);
    }

    return dataSource = ds;
  }

  /**
   * Read the JDBC url from the properties
   * @return String - JDBC URL.
   */
  private String getJdbcUrl() {

    String url = properties.getProperty(JDBC_PROP_NAME);

    if (Strings.isNullOrEmpty(url)) {
      throw new IllegalStateException("No JDBC URL found.");
    }

    return url;
  }
}
