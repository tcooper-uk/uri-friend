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

public class ConfigurationModule extends AbstractModule {

  private static final String PROPERTIES_FILE = "app.properties";
  private static final String APP_DOMAIN_PROPERTIES_KEY = "app.domain";

  private final Properties properties;
  private final String appDomain;

  public ConfigurationModule() throws IOException {
    properties = readProperties();
    appDomain = readAppDomain();
  }

  @Retention(RetentionPolicy.RUNTIME)
  public @interface AppDomain{}

  @Provides
  @AppDomain
  public String getAppDomain() {
    return appDomain;
  }

  @Provides
  public Properties getProperties() { return properties; }

  private Properties readProperties() throws IOException {

    Properties props = new Properties();
    URL url = Resources.getResource(PROPERTIES_FILE);
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
}
