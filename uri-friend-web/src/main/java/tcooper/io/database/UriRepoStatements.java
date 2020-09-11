package tcooper.io.database;

class UriRepoStatements {

    static final String INSERT_SHORT_URL_SQL = "INSERT INTO short_url (created_at, scheme_id, authority_id, relative_path_id) VALUES (?, ?, ?, ?)";
    static final String UPSERT_SCHEME_SQL = "INSERT INTO scheme (scheme) VALUES (?) ON CONFLICT(scheme) DO UPDATE SET scheme = EXCLUDED.scheme";
    static final String UPSERT_AUTHORITY_SQL = "INSERT INTO authority (authority) VALUES (?) ON CONFLICT(authority) DO UPDATE SET authority = EXCLUDED.authority";
    static final String UPSERT_RELATIVE_PATH_SQL = "INSERT INTO relative_path (path) VALUES (?) ON CONFLICT(path) DO UPDATE SET path = EXCLUDED.path";

    static final String QUERY_SCHEME_BY_ID_SQL = "SELECT scheme FROM scheme WHERE id = ?";
    static final String QUERY_AUTHORITY_BY_ID_SQL = "SELECT authority FROM authority WHERE id = ?";
    static final String QUERY_RELATIVE_PATH_BY_ID_SQL = "SELECT path FROM relative_path WHERE id = ?";
    static final String QUERY_URL_SHORT_VALUES_SQL = "SELECT s.scheme AS scheme, a.authority AS authority, rp.path AS relativePath"
        + " FROM short_url su"
        + " JOIN scheme s ON s.id = su.scheme_id"
        + " JOIN authority a ON a.id = su.authority_id"
        + " JOIN relative_path rp ON rp.id = su.relative_path_id"
        + " WHERE su.id = ?";
}
