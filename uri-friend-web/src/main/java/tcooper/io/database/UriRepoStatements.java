package tcooper.io.database;

class UriRepoStatements {

    static final String UPSERT_SCHEME_SQL = "INSERT INTO scheme (scheme) VALUES (?) ON CONFLICT(scheme) DO UPDATE SET scheme = EXCLUDED.scheme";
    static final String UPSERT_AUTHORITY_SQL = "INSERT INTO authority (authority) VALUES (?) ON CONFLICT(authority) DO UPDATE SET authority = EXCLUDED.authority";
    static final String UPSERT_RELATIVE_PATH_SQL = "INSERT INTO relative_path (path) VALUES (?) ON CONFLICT(path) DO UPDATE SET path = EXCLUDED.path";

    static final String QUERY_SCHEME_BY_ID_SQL = "SELECT scheme FROM scheme WHERE id = ?";
    static final String QUERY_AUTHORITY_BY_ID_SQL = "SELECT authority FROM authority WHERE id = ?";
    static final String QUERY_RELATIVE_PATH_BY_ID_SQL = "SELECT path FROM relative_path WHERE id = ?";
}
