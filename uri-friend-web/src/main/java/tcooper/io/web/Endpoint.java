package tcooper.io.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Basic test endpoint
 */
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
