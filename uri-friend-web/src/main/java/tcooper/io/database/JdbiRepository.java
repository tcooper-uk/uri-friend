package tcooper.io.database;

import org.jdbi.v3.core.Jdbi;

import java.sql.SQLException;

public class JdbiRepository implements UriRepository {

    private final Jdbi _jdbi;

    public JdbiRepository(String jdbcUrl) {
        _jdbi = Jdbi.create(jdbcUrl);
    }

    @Override
    public long upsertScheme(String scheme) throws SQLException {

       return _jdbi.withHandle(handle -> handle.createUpdate(UriRepoStatements.UPSERT_SCHEME_SQL)
                .bind(0, scheme)
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class)
                .one());
    }

    @Override
    public long upsertAuthority(String authority) throws SQLException {
        return _jdbi.withHandle(handle -> handle.createUpdate(UriRepoStatements.UPSERT_AUTHORITY_SQL)
                .bind(0, authority)
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class)
                .one());
    }

    @Override
    public long upsertRelativePath(String relativePath) throws SQLException {
        return _jdbi.withHandle(handle -> handle.createUpdate(UriRepoStatements.UPSERT_RELATIVE_PATH_SQL)
                .bind(0, relativePath)
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class)
                .one());
    }

    @Override
    public String getScheme(long id) throws SQLException {
        return _jdbi.withHandle(handle -> handle
                .createQuery(UriRepoStatements.QUERY_SCHEME_BY_ID_SQL)
                .bind(0, id)
                .mapTo(String.class)
                .first()
        );
    }

    @Override
    public String getAuthority(long id) throws SQLException {
        return _jdbi.withHandle(handle -> handle
                .createQuery(UriRepoStatements.QUERY_AUTHORITY_BY_ID_SQL)
                .bind(0, id)
                .mapTo(String.class)
                .first()
        );
    }

    @Override
    public String getRelativePath(long id) throws SQLException {
        return _jdbi.withHandle(handle -> handle
                .createQuery(UriRepoStatements.QUERY_RELATIVE_PATH_BY_ID_SQL)
                .bind(0, id)
                .mapTo(String.class)
                .first()
        );
    }
}
