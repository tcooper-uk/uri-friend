package tcooper.io.database;

public interface UriRepository {
    long getSchemeId(String scheme);
    long getAuthorityId(String authority);
    long getRelativePathId(String relativePath);
}
