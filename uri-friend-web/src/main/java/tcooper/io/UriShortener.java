package tcooper.io;

import tcooper.io.database.UriRepository;
import tcooper.io.uri.UriService;

import java.net.URI;

public class UriShortener {

    private UriRepository uriRepository;
    private UriService uriService;

    public UriShortener(UriRepository uriRepository, UriService uriService) {
        this.uriRepository = uriRepository;
        this.uriService = uriService;
    }

    /**
     * The job of this is to shorten the url
     * persisting the information to the database
     * @param uri
     * @return
     */
    public URI shortenUri(String uri){
        return null;
    }
}
