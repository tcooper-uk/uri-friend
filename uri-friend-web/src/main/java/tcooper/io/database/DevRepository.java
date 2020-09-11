package tcooper.io.database;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import tcooper.io.model.UriParts;

/**
 * Basic in mem store for development.
 */
@Deprecated
public class DevRepository implements UriRepository {

    private static Map<Long, String> _store;
    static {
        _store = new HashMap<>();
    }

    @Override
    public long upsertScheme(String scheme) {
        _store.put(1L, scheme);
        return 1L;
    }

    @Override
    public long upsertAuthority(String authority) {
        _store.put(3L, authority);
        return 3L;
    }

    @Override
    public long upsertRelativePath(String relativePath) {
        _store.put(5L, relativePath);
        return 5L;
    }

    @Override
    public long insertShortUrl(ZonedDateTime dateTime, long schemeId, long authorityId,
        long relativePathId) {
        return 1234;
    }

    @Override
    public UriParts getUriParts(long urlId) {
        return new UriParts("http", "test.com", "/hello");
    }

    @Override
    public String getScheme(long id) {
        return _store.get(id);
    }

    @Override
    public String getAuthority(long id) {
        return _store.get(id);
    }

    @Override
    public String getRelativePath(long id) {
        return _store.get(id);
    }
}
