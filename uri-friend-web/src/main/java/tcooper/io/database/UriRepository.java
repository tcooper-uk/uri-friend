package tcooper.io.database;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import tcooper.io.model.UriParts;

public interface UriRepository {

    long upsertScheme(String scheme) throws SQLException;
    long upsertAuthority(String authority) throws SQLException;
    long upsertRelativePath(String relativePath) throws SQLException;

    long insertShortUrl(ZonedDateTime dateTime, long schemeId, long authorityId, long relativePathId)
        throws SQLException;
    UriParts getUriParts(long urlId) throws SQLException;

    String getScheme(long id) throws SQLException;
    String getAuthority(long id) throws SQLException;
    String getRelativePath(long id) throws SQLException;
}
