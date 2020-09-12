package tcooper.io.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;
import java.time.LocalDateTime;

public class URIInfo {

    private URI originalUri;
    private URI shortUri;
    private boolean success = true;
    private String message;

    @JsonIgnore
    private LocalDateTime expiration;

    public URIInfo() {
    }

    public URIInfo(String errorMessage) {
        this.message = errorMessage;
        this.success = false;
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

    @JsonProperty("success")
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
