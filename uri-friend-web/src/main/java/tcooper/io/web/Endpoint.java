package tcooper.io.web;

import com.google.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Basic test endpoint
 */
@Singleton
@Path("/api")
public class Endpoint {

    /**
     * Ping - test endpoint
     * HTTP GET
     * @return Plain text "pong"
     */
    @GET
    @Path("ping")
    @Produces(MediaType.TEXT_PLAIN)
    public String ping(){
        return "Pong!";
    }
}
