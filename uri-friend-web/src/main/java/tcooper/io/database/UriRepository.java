package tcooper.io.database;

public interface UriRepository {

    long upsertScheme(String scheme);
    long upsertAuthority(String authority);
    long upsertRelativePath(String relativePath);

    String getScheme(long id);
    String getAuthority(long id);
    String getRelativePath(long id);
}
