package tcooper.io.guice;

import com.google.common.base.Strings;
import com.google.common.io.ByteSource;
import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URL;
import java.util.Properties;
import javax.inject.Qualifier;

public class ConfigurationModule extends AbstractModule {


  private static final String PROPERTIES_FILE_ENVIRONMENT = "app-%s.properties";
  private static final String APP_DOMAIN_PROPERTIES_KEY = "app.domain";
  private static final String APP_PORT_PROPERTIES_KEY = "app.port";

  private final String environment;
  private final Properties properties;
  private final String appDomain;
  private final int port;

  public ConfigurationModule(String environment) throws IOException {
    this.environment = environment;
    properties = readProperties();
    appDomain = readAppDomain();
    port = readPort();
  }

  @Retention(RetentionPolicy.RUNTIME)
  public @interface AppDomain{}

  @Provides
  @AppDomain
  public String getAppDomain() {
    return appDomain;
  }

  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Port {}

  @Provides
  @Port
  public int providesPort() {
    return this.port;
  }

  @Provides
  public Properties getProperties() { return properties; }

  private Properties readProperties() throws IOException {

    Properties props = new Properties();
    String propFile = String.format(PROPERTIES_FILE_ENVIRONMENT, environment);

    URL url = Resources.getResource(propFile);
    final ByteSource byteSource = Resources.asByteSource(url);

    try(InputStream inputStream = byteSource.openBufferedStream()) {
      props.load(inputStream);
      return props;
    }
  }

  private String readAppDomain() {
    String domain = properties.getProperty(APP_DOMAIN_PROPERTIES_KEY);

    if(Strings.isNullOrEmpty(domain))
      throw new IllegalStateException("App domain is required.");

    return domain;
  }

  private int readPort() {

    String port = properties.getProperty(APP_PORT_PROPERTIES_KEY);
    Integer finalPort = Integer.valueOf(port);

    if(finalPort == null)
      throw new IllegalStateException("App port is required.");

    return finalPort;
  }
}
