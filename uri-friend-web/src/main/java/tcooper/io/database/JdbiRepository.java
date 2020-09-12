package tcooper.io.database;

import com.google.inject.Inject;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import javax.sql.DataSource;
import org.jdbi.v3.core.Jdbi;
import tcooper.io.model.UriParts;

public class JdbiRepository implements UriRepository {

    private final Jdbi _jdbi;

    @Inject
    public JdbiRepository(DataSource dataSource) {

        _jdbi = Jdbi.create(dataSource);
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
    public long insertShortUrl(ZonedDateTime dateTime, long schemeId, long authorityId,
        long relativePathId) throws SQLException {

        return _jdbi.withHandle(handle ->
            handle.createUpdate(UriRepoStatements.INSERT_SHORT_URL_SQL)
            .bind(0, dateTime)
            .bind(1, schemeId)
            .bind(2, authorityId)
            .bind(3, relativePathId)
            .executeAndReturnGeneratedKeys()
            .mapTo(Long.class)
            .one()
        );
    }

    @Override
    public UriParts getUriParts(long urlId) {
        return _jdbi.withHandle(handle ->
            handle.createQuery(UriRepoStatements.QUERY_URL_SHORT_VALUES_SQL)
            .bind(0 , urlId)
            .map((rs, ctx) -> new UriParts(rs.getString(1), rs.getString(2), rs.getString(3)))
            .stream()
            .findFirst().orElse(null)
        );
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
