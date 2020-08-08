package tcooper.io;

import tcooper.io.database.UriRepository;
import tcooper.io.model.URIInfo;
import tcooper.io.uri.UriParser;
import tcooper.io.uri.UriService;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

public class UriShortener {

    private static final String BAD_URI_ERROR = "The supplied URI is invalid.";

    private UriRepository uriRepository;
    private UriService uriService;

    public UriShortener(UriRepository uriRepository, UriService uriService) {
        this.uriRepository = uriRepository;
        this.uriService = uriService;
    }

    /**
     * Translates a URI into a shortened version and persists to database.
     * @param uriStr The full uri to shorten
     * @return URIInfo - both the original and short URI.
     */
    public URIInfo shortenUri(String uriStr) throws URISyntaxException {

        var uri = parseUriString(uriStr);

        long schemeId = uriRepository.upsertScheme(uri.getScheme());
        long authorityId = uriRepository.upsertAuthority(uri.getAuthority());
        long relativePathId = uriRepository.upsertRelativePath(buildRelativePath(uri));

        URI shortUri = uriService.shortenUri(new long[] {schemeId, authorityId, relativePathId});

        return new URIInfo(uri, shortUri, LocalDateTime.now().plusDays(30));
    }

    /**
     * Translates a short URI into the full original version.
     * @param uriStr A shorten URI.
     * @return URIInfo - both the original and short URI.
     */
    public URIInfo resolveUri(String uriStr) throws URISyntaxException {
        var shortUri = parseUriString(uriStr);

        long[] ids = uriService.decomposeUri(shortUri);

        String scheme = uriRepository.getScheme(ids[0]);
        String authority = uriRepository.getAuthority(ids[1]);
        String relativePath = uriRepository.getRelativePath(ids[2]);

        if(scheme == null || authority == null || relativePath == null){
            throw new IllegalStateException("Unable to find the required items in the database");
        }

        StringBuilder builder = new StringBuilder(scheme);
        builder.append("://")
                .append(authority)
                .append(relativePath);

        var dbUriStr = builder.toString();
        var uri = UriParser.parse(dbUriStr);

        if(uri.isEmpty()){
            throw new URISyntaxException(dbUriStr, "Database values od not conform to URI standard");
        }

        return new URIInfo(uri.get(), shortUri,LocalDateTime.now());
    }

    private String buildRelativePath(URI uri) {
        StringBuilder builder = new StringBuilder(uri.getPath());

        if(uri.getQuery() != null)
            builder.append(uri.getQuery());

        if(uri.getFragment() != null)
            builder.append(uri.getFragment());

        return builder.toString();
    }

    private URI parseUriString(String inputUri) throws URISyntaxException {
        var uri = UriParser.parse(inputUri);

        if(uri.isEmpty())
            throw new URISyntaxException(inputUri, BAD_URI_ERROR);

        return uri.get();
    }
}
