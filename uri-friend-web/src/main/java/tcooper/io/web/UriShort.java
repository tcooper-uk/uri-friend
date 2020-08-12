package tcooper.io.web;

import tcooper.io.model.URIInfo;
import tcooper.io.uri.UriParser;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.net.URI;

@Path("/u")
@Produces(MediaType.APPLICATION_JSON)
public class UriShort {


    @Path("short")
    @POST
    public URIInfo shortenUri(@QueryParam("url") String url) {
        System.out.println("URL to shorten: " + url);
        URI uri = UriParser.parse(url).get();
        return new URIInfo(uri, uri);
    }

    @Path("resolve")
    @POST
    public URIInfo resolveUri(@QueryParam("url") String url){
        System.out.println("URL to resolve: " + url);
        URI uri = UriParser.parse(url).get();
        return new URIInfo(uri, uri);
    }
}
