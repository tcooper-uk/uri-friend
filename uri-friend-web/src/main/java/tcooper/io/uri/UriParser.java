package tcooper.io.uri;

import com.google.common.base.Preconditions;
import java.net.URI;
import java.util.Optional;

public class UriParser {

    public static Optional<URI> parse(String url) {

        Preconditions.checkArgument(url != null, "Url cannot be null");

        try {
            var uri = URI.create(url);
            return Optional.of(uri);
        }
        catch(IllegalArgumentException e){
            // bad URL
            return Optional.empty();
        }
    }
}