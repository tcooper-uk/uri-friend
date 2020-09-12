package tcooper.io.guice;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class ConfigurationModule extends AbstractModule {

  private static final String PROPERTIES_FILE = "app.properties";

  private final Properties properties;

  public ConfigurationModule() throws IOException {
    properties = readProperties();
  }

  @Override
  protected void configure() {
    bind(Properties.class).toInstance(properties);
  }

  private Properties readProperties() throws IOException {

    Properties props = new Properties();
    URL url = Resources.getResource(PROPERTIES_FILE);
    final ByteSource byteSource = Resources.asByteSource(url);

    try(InputStream inputStream = byteSource.openBufferedStream()) {
      props.load(inputStream);
      return props;
    }
  }

  public Properties getProperties() {
    return properties;
  }
}
