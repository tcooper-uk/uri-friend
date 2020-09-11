package tcooper.io;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import tcooper.io.database.UriRepository;
import tcooper.io.model.URIInfo;
import tcooper.io.model.UriParts;
import tcooper.io.uri.UriParser;
import tcooper.io.uri.UriService;

public class UriShortener {

    private static final char URI_QUERY_INDICATOR = '?';
    private static final char URI_FRAGMENT_INDICATOR = '#';

    private static final String BAD_URI_ERROR = "The supplied URI is invalid.";

    private final UriRepository uriRepository;
    private final UriService uriService;

    @Inject
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

        try {
            long schemeId = uriRepository.upsertScheme(uri.getScheme());
            long authorityId = uriRepository.upsertAuthority(uri.getAuthority());
            long relativePathId = uriRepository.upsertRelativePath(buildRelativePath(uri));
            long shortUrlId = uriRepository.insertShortUrl(ZonedDateTime.now(), schemeId, authorityId, relativePathId);

            //URI shortUri = uriService.shortenUri(new long[] {schemeId, authorityId, relativePathId});
            URI shortUri = uriService.shortenUri(shortUrlId);

            return new URIInfo(uri, shortUri, LocalDateTime.now().plusDays(30));

        } catch (SQLException e) {
            return new URIInfo();
        }
    }

    /**
     * Translates a short URI into the full original version.
     * @param uriStr A shorten URI.
     * @return URIInfo - both the original and short URI.
     */
    public URIInfo resolveUri(String uriStr) throws URISyntaxException {
        var shortUri = parseUriString(uriStr);

        //long[] ids = uriService.decomposeShortUri(shortUri);
        long uriId = uriService.decomposeShortUriV2(shortUri);

        try {
            UriParts parts = uriRepository.getUriParts(uriId);

            if(parts == null){
                throw new IllegalStateException("Unable to find the required items in the database");
            }

            StringBuilder builder = new StringBuilder(parts.getScheme());
            builder.append("://")
                    .append(parts.getAuthority())
                    .append(parts.getRelativePath());

            var dbUriStr = builder.toString();
            var uri = UriParser.parse(dbUriStr);

            if(uri.isEmpty()){
                throw new URISyntaxException(dbUriStr, "Database values od not conform to URI standard");
            }

            return new URIInfo(uri.get(), shortUri,LocalDateTime.now());

        } catch (SQLException e) {
            return new URIInfo();
        }
    }

    private String buildRelativePath(URI uri) {
        StringBuilder builder = new StringBuilder(uri.getRawPath());

        if(uri.getQuery() != null) {
            builder.append(URI_QUERY_INDICATOR);
            builder.append(uri.getRawQuery());
        }

        if(uri.getFragment() != null) {
            builder.append(URI_FRAGMENT_INDICATOR);
            builder.append(uri.getRawFragment());
        }

        return builder.toString();
    }

    private URI parseUriString(String inputUri) throws URISyntaxException {
        var uriOpt = UriParser.parse(inputUri);

        boolean isValid = true;
        isValid &= uriOpt.isPresent();

        if(isValid){
            var uri = uriOpt.get();
            isValid &= !Strings.isNullOrEmpty(uri.getScheme());
            isValid &= !Strings.isNullOrEmpty(uri.getAuthority());
        }

        if(!isValid)
            throw new URISyntaxException(inputUri, BAD_URI_ERROR);

        return uriOpt.get();
    }
}
