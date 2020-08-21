package tcooper.io.database;

import java.sql.SQLException;

public interface UriRepository {

    long upsertScheme(String scheme) throws SQLException;
    long upsertAuthority(String authority) throws SQLException;
    long upsertRelativePath(String relativePath) throws SQLException;

    String getScheme(long id) throws SQLException;
    String getAuthority(long id) throws SQLException;
    String getRelativePath(long id) throws SQLException;
}
