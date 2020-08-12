package tcooper.io.web;


import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class WebApplication extends Application {

    Set<Class<?>> classes = new HashSet<>();

    public WebApplication() {
        classes.add(Endpoint.class);
        classes.add(UriShort.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}
