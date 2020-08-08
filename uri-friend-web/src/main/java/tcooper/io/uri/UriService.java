package tcooper.io.uri;

import com.google.common.primitives.Longs;

import java.net.URI;
import java.util.Random;


public class UriService {

    // We will move this out somewhere else
    private static final String NEW_AUTHORITY = "http://example.com";

    /**
     * Take a previously shortened URI and gathers each database ID
     * @param uri A uri previously shortened
     * @return Array of database ID's
     */
    public long[] decomposeUri(URI uri){

        if(uri == null)
            throw new IllegalArgumentException("Uri cannot be null");

        var fullPath = uri.getPath();
        var parts = fullPath.split("/");
        var path = parts[parts.length - 1];

        long[] results = new long[3];
        int resultCounter = 0;

        int index = 0;
        int lastAlphaIndex = 0;

        for(char c : path.toCharArray()) {

            String idStr = null;

            if(index == path.length() - 1 && Character.isDigit(c)){
                // must be last id
                idStr = path.substring(lastAlphaIndex, path.length());
            }

            // look for delimiter
            if(Character.isAlphabetic(c)){
                idStr = path.substring(lastAlphaIndex, index);
                lastAlphaIndex = index + 1;
            }


            if(idStr != null) {

                var lValue = Longs.tryParse(idStr);
                if (lValue == null)
                    raiseUriFormatError();

                results[resultCounter++] = lValue;

                if (resultCounter == results.length)
                    break;
            }

            index++;
        }

        if(resultCounter != results.length)
            raiseUriFormatError();

        return results;
    }

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
    private void raiseUriFormatError() throws IllegalArgumentException {
        throw new IllegalArgumentException("The given short URI was not in a valid format");
    }
}
