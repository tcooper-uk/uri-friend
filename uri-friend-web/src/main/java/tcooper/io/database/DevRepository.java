package tcooper.io.database;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic in mem store for development.
 */
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
