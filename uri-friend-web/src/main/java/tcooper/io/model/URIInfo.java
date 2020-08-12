package tcooper.io.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.net.URI;
import java.time.LocalDateTime;

public class URIInfo {

    private URI originalUri;
    private URI shortUri;

    @JsonIgnore
    private LocalDateTime expiration;

    public URIInfo(URI originalUri, URI shortUri) {
        this.originalUri = originalUri;
        this.shortUri = shortUri;
        expiration = LocalDateTime.now();
    }

    public URIInfo(URI originalUri, URI shortUri, LocalDateTime expiration) {
        this.originalUri = originalUri;
        this.shortUri = shortUri;
        this.expiration = expiration;
    }

    public URI getOriginalUri() {
        return originalUri;
    }

    public URI getShortUri() {
        return shortUri;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }
}
