package tcooper.io.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.DriverManager.getConnection;
import static tcooper.io.database.UriRepoStatements.UPSERT_AUTHORITY_SQL;
import static tcooper.io.database.UriRepoStatements.UPSERT_RELATIVE_PATH_SQL;

/**
 * Repository basic JDBC implementation for local postgres db.
 */
public class JdbcPostgres implements UriRepository {

    private static Connection _dbConnection;

    static {
        try {
            _dbConnection = getConnection("jdbc:postgresql:uri_short");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long upsertScheme(String scheme) throws SQLException {

        var statement = _dbConnection.prepareStatement(UriRepoStatements.UPSERT_SCHEME_SQL, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, scheme);

        statement.executeUpdate();

        ResultSet resultSet = statement.getGeneratedKeys();
        if(resultSet.next()){
            return resultSet.getLong(1);
        }

        return -1L;
    }

    @Override
    public long upsertAuthority(String authority) throws SQLException {

        var statement = _dbConnection.prepareStatement(UPSERT_AUTHORITY_SQL, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, authority);

        statement.executeUpdate();

        ResultSet resultSet = statement.getGeneratedKeys();
        if(resultSet.next()){
            return resultSet.getLong(1);
        }

        return -1L;
    }

    @Override
    public long upsertRelativePath(String relativePath) throws SQLException {

        var statement = _dbConnection.prepareStatement(UPSERT_RELATIVE_PATH_SQL, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, relativePath);

        statement.executeUpdate();

        ResultSet resultSet = statement.getGeneratedKeys();
        if(resultSet.next()){
            return resultSet.getLong(1);
        }

        return -1L;
    }

    @Override
    public String getScheme(long id) throws SQLException {

        var statement = _dbConnection.prepareStatement(UriRepoStatements.QUERY_SCHEME_BY_ID_SQL);
        statement.setLong(1, id);

        ResultSet results = statement.executeQuery();

        if(results.next()){
            return results.getString(1);
        }

        return null;
    }

    @Override
    public String getAuthority(long id) throws SQLException {

        var statement = _dbConnection.prepareStatement(UriRepoStatements.QUERY_AUTHORITY_BY_ID_SQL);
        statement.setLong(1, id);

        ResultSet results = statement.executeQuery();

        if(results.next()){
            return results.getString(1);
        }

        return null;
    }

    @Override
    public String getRelativePath(long id) throws SQLException {

        var statement = _dbConnection.prepareStatement(UriRepoStatements.QUERY_RELATIVE_PATH_BY_ID_SQL);
        statement.setLong(1, id);

        ResultSet results = statement.executeQuery();

        if(results.next()){
            return results.getString(1);
        }

        return null;
    }
}
