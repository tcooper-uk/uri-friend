package tcooper.io.web;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import tcooper.io.UriShortener;
import tcooper.io.database.JdbcPostgres;
import tcooper.io.database.JdbiRepository;
import tcooper.io.database.UriRepository;
import tcooper.io.model.URIInfo;
import tcooper.io.uri.UriService;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.net.URISyntaxException;
import java.sql.SQLException;

@Singleton
@Path("/u")
@Produces(MediaType.APPLICATION_JSON)
public class UriShort {

    private final UriShortener uriShortener;

    /**
     * Setup controller with injected deps
     * @param uriShortener
     */
    @Inject
    public UriShort(UriShortener uriShortener) {
        this.uriShortener = uriShortener;
    }

    @Path("short")
    @POST
    public URIInfo shortenUri(@QueryParam("url") String url) {
        System.out.println("URL to shorten: " + url);

        try {
            return uriShortener.shortenUri(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return new URIInfo();
        }
    }

    @Path("resolve")
    @POST
    public URIInfo resolveUri(@QueryParam("url") String url){
        System.out.println("URL to resolve: " + url);

        try {
            return uriShortener.resolveUri(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return new URIInfo();
        }
    }
}
