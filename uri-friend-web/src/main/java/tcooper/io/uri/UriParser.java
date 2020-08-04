package tcooper.io.uri;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.Optional;

public class UriParser {

    public static Optional<URI> parse(@NotNull String url) {

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
