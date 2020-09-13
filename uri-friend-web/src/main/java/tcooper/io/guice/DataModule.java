package tcooper.io.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import javax.sql.DataSource;
import tcooper.io.database.JdbiRepository;
import tcooper.io.database.UriRepository;

public class DataModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(DataSource.class).toProvider(DataSourceProvider.class).in(Singleton.class);
    bind(UriRepository.class).to(JdbiRepository.class);
  }

}
