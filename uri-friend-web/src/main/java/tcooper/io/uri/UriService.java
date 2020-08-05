package tcooper.io.uri;

import java.net.URI;
import java.util.Random;

public class UriService {

    // We will move this out somewhere else
    private static final String NEW_AUTHORITY = "http://example.com";

    /**
     * Method to id's of URI parts, concant these, and return the new URI
     * @param uriPartIds a array of id [scheme, authority, relativePath,...]
     * @return a new URI
     */
    public URI shortenUri(long[] uriPartIds) {

        if(uriPartIds.length < 3)
            throw new IllegalArgumentException("You did not supply enough ID's");

        StringBuilder builder = new StringBuilder(NEW_AUTHORITY);

        builder
                .append('/')
                .append(uriPartIds[0])
                .append(getUriChar())
                .append(uriPartIds[1])
                .append(getUriChar())
                .append(uriPartIds[2]);

        var newUriResult = UriParser.parse(builder.toString());

        return newUriResult.isPresent() ? newUriResult.get() : null;
    }

    private char getUriChar() {
        Random r = new Random();
        return (char)(r.nextInt(26) + 'a');
    }
}
